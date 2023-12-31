package com.example.sportstore06.controller;


import com.example.sportstore06.dao.Statistic;
import com.example.sportstore06.dao.response.BillResponse;
import com.example.sportstore06.dao.response.MomoResponse;
import com.example.sportstore06.entity.*;
import com.example.sportstore06.service.*;
import com.example.sportstore06.service.MomoService.MomoPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/v1/bill")
@RequiredArgsConstructor
public class BillController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final BillService billService;
    private final UserService userService;
    private final ProductService productService;
    private final BusinessService businessService;
    private final MomoPaymentService momoPaymentService;
    private final TransactionService transactionService;

    // 0 : đang giao
    // 1 : đã giao thành công
    // 2 : chưa thanh toán
    // 3 : đã thanh toán
    // 4 : hủy đơn hàng bên business

    // 2 -> 3 -> 0 -> 1
    // 2 -> 3 -> 4 -> (hoàn tiền)

    //  NGUYEN VAN A
    //  9704 0000 0000 0018
    //  03/07
    //  OTP
    //  requestType : payWithATM,captureWallet

    @GetMapping("/get-count")
    public ResponseEntity<?> getCount() {
        return ResponseEntity.status(HttpStatus.OK).body(billService.getCount());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            if (billService.findById(id).isPresent()) {
                BillResponse b = new BillResponse(billService.findById(id).get());
                return ResponseEntity.status(HttpStatus.OK).body(b);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id bill not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("get-by-id-user/{id_user}")
    public ResponseEntity<?> findByIdUser(@PathVariable("id_user") Integer id_user,
                                          @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            if (userService.findById(id_user).isPresent()) {
                List<Bill> byIdUser = billService.findByIdUser(id_user, state.orElse(null));
                List<BillResponse> responses = new ArrayList<>();
                for (Bill bill : byIdUser) {
                    responses.add(new BillResponse(bill));
                }
                return ResponseEntity.status(HttpStatus.OK).body(responses);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "name", required = true) String name,
                                    @RequestParam(value = "page", required = false) Optional<Integer> page,
                                    @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                    @RequestParam(value = "sort", required = false) String sort,
                                    @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                    @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Bill> byPage;
            if (state.isPresent()) {
                byPage = billService.SearchByName(pageable, name, state.get());
            } else {
                byPage = billService.SearchByName(pageable, name);
            }
            Page<BillResponse> responses = byPage.map(bill -> bill != null ? new BillResponse(bill) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping()
    public ResponseEntity<?> findByPage(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                        @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                        @RequestParam(value = "sort", required = false) String sort,
                                        @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                        @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Bill> byPage;
            if (state.isPresent()) {
                byPage = billService.findByPage(pageable, state.get());
            } else {
                byPage = billService.findByPage(pageable);
            }
            Page<BillResponse> responses = byPage.map(bill -> bill != null ? new BillResponse(bill) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping("get-by-business/{id_business}")
    public ResponseEntity<?> findByBusiness(
            @PathVariable(value = "id_business", required = true) Integer id_business,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
            @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            if (businessService.findById(id_business).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
            }
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Bill> byPage;
            byPage = billService.findByIdBusiness(pageable, id_business, state.orElse(null));
            Page<BillResponse> responses = byPage.map(bill -> bill != null ? new BillResponse(bill) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping("/get_refresh_payment/{id_transaction}")
    public ResponseEntity<?> getRefreshPayment(
            @PathVariable(value = "id_transaction", required = true) Integer id_transaction,
            @AuthenticationPrincipal User user) {
        if (transactionService.findById(id_transaction).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id transaction not found");
        }
        Transaction transaction = transactionService.findById(id_transaction).get();
        return ResponseEntity.status(HttpStatus.OK).body(transaction.getPayUrl());
    }

    @PutMapping("/change-state/{id}")
    private ResponseEntity<?> changeState(@PathVariable("id") Integer id,
                                          @RequestParam(value = "state", required = true) Integer state) {
        try {
            if (billService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id bill not found");
            } else if (state < 0 || state > 3) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("state not found");
            } else {
                billService.changeState(id, state);
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/confirm-sell/{sell}")
    private ResponseEntity<?> confirmSell(@PathVariable(value = "sell", required = true) Boolean sell,
                                          @RequestBody(required = true) Set<Integer> set_id_bill) {
        try {
            for (Integer id : set_id_bill) {
                if (billService.findById(id).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id bill not found");
                }
                if (billService.findById(id).get().getState() != 3) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The bill status needs confirmation from the business");
                } else {
                    if (sell) {
                        Bill bill = billService.findById(id).get();
                        bill.setState(0);
                        bill.setUpdated_at(new Timestamp(new Date().getTime()));
                        // check quantity
                        for (BillDetail billDetail : bill.getBill_detailSet()) {
                            Product product = billDetail.getProduct();
                            Integer quantity = product.getQuantity();
                            Integer quantity_change = quantity - billDetail.getQuantity();
                            if (quantity_change < 0) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(String.valueOf(product.getId()) + " : quantity not sufficient");
                            }
                        }
                        // change quantity
                        bill.getBill_detailSet().forEach(billDetail -> {
                            Product product = billDetail.getProduct();
                            Integer quantity = product.getQuantity();
                            Integer quantity_change = quantity - billDetail.getQuantity();
                            product.setQuantity(quantity_change);
                            productService.save(product);
                        });
                        billService.save(bill);
                    } else {
                        Bill bill = billService.findById(id).get();
                        //hoan tien
                        MomoResponse momoResponse = momoPaymentService.refundTransactionStatus(bill);
                        if (momoResponse.getResultCode().equals("0") || momoResponse.getResultCode().equals("1002")) {
                            bill.setState(4);
                            bill.setUpdated_at(new Timestamp(new Date().getTime()));
                            billService.save(bill);
                            return ResponseEntity.status(HttpStatus.OK).body(momoResponse);
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(momoResponse);
                        }
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/confirm-receive/{receive}")
    private ResponseEntity<?> confirmReceive(@RequestBody(required = true) Set<Integer> set_id_bill,
                                             @PathVariable(value = "receive", required = true) Boolean receive) {
        try {
            for (Integer id : set_id_bill) {
                if (billService.findById(id).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id bill not found");
                }
                if (billService.findById(id).get().getState() != 0) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("bill status must be shipping");
                } else {
                    if (receive) {
                        Bill bill = billService.findById(id).get();
                        bill.setState(1);
                        bill.setUpdated_at(new Timestamp(new Date().getTime()));
                        billService.save(bill);

                        //chuyển tiền
                        Business business = businessService.findById(bill.getId_business()).get();
                        Long revenue = business.getRevenue();
                        revenue = (long) Math.round((revenue + bill.getTotal()) * 0.95);
                        business.setRevenue(revenue);
                        businessService.save(business);
                        //
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/confirm-buy/{buy}")
    private ResponseEntity<?> confirmBuy(@RequestBody(required = true) Integer id_bill,
                                         @PathVariable(value = "buy", required = true) Boolean buy) {
        if (!buy) {
            if (billService.findById(id_bill).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id bill not found");
            }
            Bill bill = billService.findById(id_bill).get();
            if (bill.getState() == 3) {
                //hoan tien
                MomoResponse momoResponse = momoPaymentService.refundTransactionStatus(bill);
                if (momoResponse.getResultCode().equals("0") || momoResponse.getResultCode().equals("1002")) {
                    bill.setState(4);
                    bill.setUpdated_at(new Timestamp(new Date().getTime()));
                    billService.save(bill);
                    return ResponseEntity.status(HttpStatus.OK).body(momoResponse);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(momoResponse);
                }
            }
            if (bill.getState() == 2) {
                bill.setState(4);
                bill.setUpdated_at(new Timestamp(new Date().getTime()));
                billService.save(bill);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @PutMapping("/confirm-cancel/{cancel}")
    private ResponseEntity<?> confirmCancel(@RequestBody(required = true) Integer id_transaction,
                                             @PathVariable(value = "cancel", required = true) Boolean cancel) {
        if (cancel) {
            if (transactionService.findById(id_transaction).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id transaction not found");
            }
            List<Bill> bills = billService.findByTransactionId(id_transaction);
            //check state
            for (Bill bill : bills) {
                Integer state = bill.getState();
                if (state == 0 || state == 1) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("bill status must not be shipping");
                }
            }

            for (Bill bill : bills) {
                    if (bill.getState() == 3) {
                        //hoan tien
                        MomoResponse momoResponse = momoPaymentService.refundTransactionStatus(bill);
                        if (momoResponse.getResultCode().equals("0") || momoResponse.getResultCode().equals("1002")) {
                            bill.setState(4);
                            bill.setUpdated_at(new Timestamp(new Date().getTime()));
                            billService.save(bill);
                            return ResponseEntity.status(HttpStatus.OK).body(momoResponse);
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(momoResponse);
                        }
                    }
                    if (bill.getState() == 2) {
                        bill.setState(4);
                        bill.setUpdated_at(new Timestamp(new Date().getTime()));
                        billService.save(bill);
                        return ResponseEntity.status(HttpStatus.OK).build();
                    }
            }
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        try {
            if (billService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id bill not found");
            } else {
                boolean checkDelete = billService.deleteById(id);
                if (!checkDelete) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("can't delete");
                }
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public Map<Integer, List<Integer>> caculaterYearMonthMap(Timestamp startDate, Timestamp endDate) {
        Map<Integer, List<Integer>> yearMonthMap = new HashMap<>();
        int startYear = Year.from(startDate.toInstant().atZone(ZoneId.systemDefault())).getValue();
        int endYear = Year.from(endDate.toInstant().atZone(ZoneId.systemDefault())).getValue();
        for (int year = startYear; year <= endYear; year++) {
            List<Integer> months = new ArrayList<>();
            int startMonth = (year == startYear) ? Month.from(startDate.toInstant().atZone(ZoneId.systemDefault())).getValue() : 1;
            int endMonth = (year == endYear) ? Month.from(endDate.toInstant().atZone(ZoneId.systemDefault())).getValue() : 12;
            for (int month = startMonth; month <= endMonth; month++) {
                months.add(month);
            }
            yearMonthMap.put(year, months);
        }
        return yearMonthMap;
    }

    public Map<Integer, List<Integer>> caculaterYearMonthMapAll(Timestamp startDate, Timestamp endDate) {

        Map<Integer, List<Integer>> yearMonthMap = new HashMap<>();
        Set<Integer> setYear = new HashSet<>();
        List<Integer> months = new ArrayList<>();

        int earlyear = Year.from(startDate.toInstant().atZone(ZoneId.systemDefault())).getValue();
        int lateyear = Year.from(endDate.toInstant().atZone(ZoneId.systemDefault())).getValue();
        int than = lateyear - earlyear;
        for (int i = 0; i <= than; i++) {
            setYear.add(earlyear);
            earlyear += 1;
        }
        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }
        for (Integer year : setYear) {
            yearMonthMap.put(year, months);
        }
        return yearMonthMap;
    }

    public Timestamp convertTime(Optional<String> time) {
        LocalDate localDate = LocalDate.parse(time.orElseGet(() -> LocalDate.now().toString()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        return Timestamp.valueOf(localDateTime);
    }

    public static boolean isValidFormat(String dateStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(dateStr, dateFormatter);
            return true; // Parsing successful, valid format
        } catch (Exception e) {
            return false; // Parsing failed, invalid format
        }
    }

    public static boolean isStartDateBeforeEndDate(Timestamp startDateGet, Timestamp endDateGet) {
        return startDateGet.before(endDateGet);
    }

    @GetMapping("/statistic")
    public ResponseEntity<?> statistic(
            @RequestParam(value = "state", required = false) Optional<Integer> state,
            @RequestParam(value = "idBusiness", required = false) Optional<Integer> idBusiness,
            @RequestParam(value = "startDate", required = false) Optional<String> startDate,
            @RequestParam(value = "endDate", required = false) Optional<String> endDate) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            double billCountAll = 0;
            double billTotalAll = 0;
            Timestamp earliestUpdate = billService.getEarliestUpdateAt();
            Timestamp latestUpdate = billService.getLatestUpdateAt();
            Set<Statistic> setStatistic = new TreeSet<>(Comparator.comparing(Statistic::getYear).thenComparing(Statistic::getMonth));
            Map<Integer, List<Integer>> yearMonthMap = new HashMap<>();
            if (idBusiness.isPresent()) {
                if (startDate.isPresent() || endDate.isPresent()) {
                    //check convert
                    if (!isValidFormat(startDate.get()) || !isValidFormat(endDate.get())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("can't convert {startDate} & {endDate} to DateTime (yyyy-MM-dd)");
                    }
                    //convert time
                    Timestamp startDateGet = convertTime(startDate);
                    Timestamp endDateGet = convertTime(endDate);


                    //check startDate and endDate
                    if (!isStartDateBeforeEndDate(startDateGet, endDateGet)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("start date can't be after end date");
                    }
                    yearMonthMap = caculaterYearMonthMap(startDateGet, endDateGet);
                    for (Integer year : yearMonthMap.keySet()) {
                        List<Integer> months = yearMonthMap.get(year);
                        for (Integer month : months) {
                            LocalDate localDateStart = LocalDate.of(year, month, 1);
                            LocalDate localDateEnd = LocalDate.of(year, month, localDateStart.lengthOfMonth());
                            Timestamp dateStart = Timestamp.valueOf(localDateStart.atStartOfDay());
                            Timestamp dateEnd = Timestamp.valueOf(localDateEnd.atStartOfDay());

                            long billCountMonth = billService.getAllCountBusiness(dateStart, dateEnd, state.orElse(null), idBusiness.get());
                            long billTotalMonth = billService.getAllTotalBusiness(dateStart, dateEnd, state.orElse(null), idBusiness.get());
                            Statistic statistic = new Statistic(year, month, billCountMonth, billTotalMonth);
                            setStatistic.add(statistic);
                            billCountAll += billCountMonth;
                            billTotalAll += billTotalMonth;
                        }
                    }
                }
                else {
                    yearMonthMap = caculaterYearMonthMapAll(earliestUpdate, latestUpdate);
                    for (Integer year : yearMonthMap.keySet()) {
                        List<Integer> months = yearMonthMap.get(year);
                        for (Integer month : months) {
                            LocalDate localDateStart = LocalDate.of(year, month, 1);
                            LocalDate localDateEnd = LocalDate.of(year, month, localDateStart.lengthOfMonth());
                            Timestamp dateStart = Timestamp.valueOf(localDateStart.atStartOfDay());
                            Timestamp dateEnd = Timestamp.valueOf(localDateEnd.atStartOfDay());

                            long billCountMonth = billService.getAllCountBusiness(dateStart, dateEnd, state.orElse(null), idBusiness.get());
                            long billTotalMonth = billService.getAllTotalBusiness(dateStart, dateEnd, state.orElse(null), idBusiness.get());
                            Statistic statistic = new Statistic(year, month, billCountMonth, billTotalMonth);

                            setStatistic.add(statistic);
                            billCountAll += billCountMonth;
                            billTotalAll += billTotalMonth;
                        }
                    }
                }
            }
            else {
                if (startDate.isPresent() || endDate.isPresent()) {;
                    //check convert
                    if (!isValidFormat(startDate.get()) || !isValidFormat(endDate.get())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("can't convert {startDate} & {endDate} to DateTime (yyyy-MM-dd)");
                    }
                    //convert time
                    Timestamp startDateGet = convertTime(startDate);
                    Timestamp endDateGet = convertTime(endDate);

                    //check startDate and endDate
                    if (!isStartDateBeforeEndDate(startDateGet, endDateGet)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("start date can't be after end date");
                    }
                    yearMonthMap = caculaterYearMonthMap(startDateGet, endDateGet);
                    for (Integer year : yearMonthMap.keySet()) {
                        List<Integer> months = yearMonthMap.get(year);
                        for (Integer month : months) {
                            LocalDate localDateStart = LocalDate.of(year, month, 1);
                            LocalDate localDateEnd = LocalDate.of(year, month, localDateStart.lengthOfMonth());
                            Timestamp dateStart = Timestamp.valueOf(localDateStart.atStartOfDay());
                            Timestamp dateEnd = Timestamp.valueOf(localDateEnd.atStartOfDay());

                            long billCountMonth = billService.getAllCount(dateStart, dateEnd, state.orElse(null));
                            long billTotalMonth = billService.getAllTotal(dateStart, dateEnd, state.orElse(null));
                            Statistic statistic = new Statistic(year, month, billCountMonth, billTotalMonth);
                            setStatistic.add(statistic);
                            billCountAll += billCountMonth;
                            billTotalAll += billTotalMonth;
                        }
                    }
                }
                else {
                    yearMonthMap = caculaterYearMonthMapAll(earliestUpdate, latestUpdate);
                    for (Integer year : yearMonthMap.keySet()) {
                        List<Integer> months = yearMonthMap.get(year);
                        for (Integer month : months) {
                            LocalDate localDateStart = LocalDate.of(year, month, 1);
                            LocalDate localDateEnd = LocalDate.of(year, month, localDateStart.lengthOfMonth());
                            Timestamp dateStart = Timestamp.valueOf(localDateStart.atStartOfDay());
                            Timestamp dateEnd = Timestamp.valueOf(localDateEnd.atStartOfDay());

                            long billCountMonth = billService.getAllCount(dateStart, dateEnd, state.orElse(null));
                            long billTotalMonth = billService.getAllTotal(dateStart, dateEnd, state.orElse(null));
                            Statistic statistic = new Statistic(year, month, billCountMonth, billTotalMonth);
                            setStatistic.add(statistic);
                            billCountAll += billCountMonth;
                            billTotalAll += billTotalMonth;
                        }
                    }
                }
            }
            map.put("bill_count_all", billCountAll);
            map.put("bill_total_all", billTotalAll);
            map.put("setStatistic", setStatistic);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping("/check-transaction-status/{id_transaction}")
    public ResponseEntity<?> checkTransactionStatus(@PathVariable(value = "id_transaction", required = true) Integer id_transaction) {
        if (transactionService.findById(id_transaction).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id transaction not found");
        }
        Transaction transaction = transactionService.findById(id_transaction).get();
        MomoResponse momoResponse = momoPaymentService.checkTransactionStatus(transaction);
        return ResponseEntity.status(HttpStatus.OK).body(momoResponse);
    }
}
