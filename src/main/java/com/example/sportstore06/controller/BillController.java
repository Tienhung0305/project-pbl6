package com.example.sportstore06.controller;


import com.example.sportstore06.dao.Statistic;
import com.example.sportstore06.dao.StatisticDate;
import com.example.sportstore06.dao.response.BillResponse;
import com.example.sportstore06.entity.Bill;
import com.example.sportstore06.service.BillService;
import com.example.sportstore06.service.ProductService;
import com.example.sportstore06.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
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

    // 0 : đang giao
    // 1 : đã giao thành công
    // 2 : chưa thanh toán

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
    public ResponseEntity<?> findByIdUser(@PathVariable("id_user") Integer id_user) {
        try {
            if (userService.findById(id_user).isPresent()) {
                List<Bill> byIdUser = billService.findByIdUser(id_user);
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

    @PutMapping("/confirm/{id}")
    private ResponseEntity<?> confirm(@PathVariable("id") Integer id,
                                      @RequestParam(value = "state", required = true) Integer state) {
        try {
            if (billService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id bill not found");
            } else if (state != 1) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("state confirm not found");
            } else {
                billService.changeState(id, state);
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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


    @GetMapping("/statistic")
    public ResponseEntity<?> statistic(
            @RequestParam(value = "state", required = false) Optional<Integer> state, 
            @RequestParam(value = "idBusiness", required = false) Optional<Integer> idBusiness, 
            @RequestBody(required = false) Optional<StatisticDate> statisticDate) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            double billCountAll = 0;
            double billTotalAll = 0;
            Timestamp earliestUpdate = billService.getEarliestUpdateAt();
            Timestamp latestUpdate = billService.getLatestUpdateAt();

            Set<Statistic> setStatistic = new TreeSet<>(Comparator.comparing(Statistic::getYear).thenComparing(Statistic::getMonth));
            Map<Integer, List<Integer>> yearMonthMap = new HashMap<>();


            if (idBusiness.isPresent()) {
                if (statisticDate.isPresent()) {
                    StatisticDate statisticDateGet = statisticDate.get();
                    billCountAll = billService.getAllCountBusiness(statisticDateGet.getStartDate(), statisticDateGet.getEndDate(), state.orElse(null), idBusiness.get());
                    billTotalAll = billService.getAllTotalBusiness(statisticDateGet.getStartDate(), statisticDateGet.getEndDate(), state.orElse(null), idBusiness.get());

                    yearMonthMap = caculaterYearMonthMapAll(statisticDateGet.getStartDate(), statisticDateGet.getEndDate());
                    for (Integer year : yearMonthMap.keySet()) {
                        List<Integer> months = yearMonthMap.get(year);
                        for (Integer month : months) {
                            LocalDate localDateStart = LocalDate.of(year, month, 1);
                            LocalDate localDateEnd = LocalDate.of(year, month, localDateStart.lengthOfMonth());
                            Timestamp dateStart = Timestamp.valueOf(localDateStart.atStartOfDay());
                            Timestamp dateEnd = Timestamp.valueOf(localDateEnd.atStartOfDay());

                            double billCountMonth = billService.getAllCountBusiness(dateStart, dateEnd, state.orElse(null), idBusiness.get());
                            double billTotalMonth = billService.getAllTotalBusiness(dateStart, dateEnd, state.orElse(null), idBusiness.get());
                            Statistic statistic = new Statistic(year, month, billCountMonth, billTotalMonth);
                            setStatistic.add(statistic);
                        }
                    }
                }
                else {
                    billCountAll = billService.getAllCountBusiness(state.orElse(null), idBusiness.get());
                    billTotalAll = billService.getAllTotalBusiness(state.orElse(null), idBusiness.get());

                    yearMonthMap = caculaterYearMonthMapAll(earliestUpdate, latestUpdate);
                    for (Integer year : yearMonthMap.keySet()) {
                        List<Integer> months = yearMonthMap.get(year);
                        for (Integer month : months) {
                            LocalDate localDateStart = LocalDate.of(year, month, 1);
                            LocalDate localDateEnd = LocalDate.of(year, month, localDateStart.lengthOfMonth());
                            Timestamp dateStart = Timestamp.valueOf(localDateStart.atStartOfDay());
                            Timestamp dateEnd = Timestamp.valueOf(localDateEnd.atStartOfDay());

                            double billCountMonth = billService.getAllCountBusiness(dateStart, dateEnd, state.orElse(null), idBusiness.get());
                            double billTotalMonth = billService.getAllTotalBusiness(dateStart, dateEnd, state.orElse(null), idBusiness.get());
                            Statistic statistic = new Statistic(year, month, billCountMonth, billTotalMonth);
                            setStatistic.add(statistic);
                        }
                    }
                }
            }
            else {
                if (statisticDate.isPresent()) {
                    StatisticDate statisticDateGet = statisticDate.get();
                    billCountAll = billService.getAllCount(statisticDateGet.getStartDate(), statisticDateGet.getEndDate(), state.orElse(null));
                    billTotalAll = billService.getAllTotal(statisticDateGet.getStartDate(), statisticDateGet.getEndDate(), state.orElse(null));

                    yearMonthMap = caculaterYearMonthMapAll(statisticDateGet.getStartDate(), statisticDateGet.getEndDate());
                    for (Integer year : yearMonthMap.keySet()) {
                        List<Integer> months = yearMonthMap.get(year);
                        for (Integer month : months) {
                            LocalDate localDateStart = LocalDate.of(year, month, 1);
                            LocalDate localDateEnd = LocalDate.of(year, month, localDateStart.lengthOfMonth());
                            Timestamp dateStart = Timestamp.valueOf(localDateStart.atStartOfDay());
                            Timestamp dateEnd = Timestamp.valueOf(localDateEnd.atStartOfDay());

                            double billCountMonth = billService.getAllCount(dateStart, dateEnd, state.orElse(null));
                            double billTotalMonth = billService.getAllTotal(dateStart, dateEnd, state.orElse(null));
                            Statistic statistic = new Statistic(year, month, billCountMonth, billTotalMonth);
                            setStatistic.add(statistic);
                        }
                    }
                }
                else {
                    billCountAll = billService.getAllCount(state.orElse(null));
                    billTotalAll = billService.getAllTotal(state.orElse(null));

                    yearMonthMap = caculaterYearMonthMapAll(earliestUpdate, latestUpdate);
                    for (Integer year : yearMonthMap.keySet()) {
                        List<Integer> months = yearMonthMap.get(year);
                        for (Integer month : months) {
                            LocalDate localDateStart = LocalDate.of(year, month, 1);
                            LocalDate localDateEnd = LocalDate.of(year, month, localDateStart.lengthOfMonth());
                            Timestamp dateStart = Timestamp.valueOf(localDateStart.atStartOfDay());
                            Timestamp dateEnd = Timestamp.valueOf(localDateEnd.atStartOfDay());

                            double billCountMonth = billService.getAllCount(dateStart, dateEnd, state.orElse(null));
                            double billTotalMonth = billService.getAllTotal(dateStart, dateEnd, state.orElse(null));
                            Statistic statistic = new Statistic(year, month, billCountMonth, billTotalMonth);
                            setStatistic.add(statistic);
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
}
