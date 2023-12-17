package com.example.sportstore06.controller;


import com.example.sportstore06.dao.request.BillDetailRequest;
import com.example.sportstore06.dao.request.BillRequest;
import com.example.sportstore06.dao.response.BillResponse;
import com.example.sportstore06.dao.response.UserResponse;
import com.example.sportstore06.model.Bill;
import com.example.sportstore06.model.Business;
import com.example.sportstore06.model.User;
import com.example.sportstore06.service.BillService;
import com.example.sportstore06.service.ProductService;
import com.example.sportstore06.service.UserService;
import jakarta.validation.Valid;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/bill")
@RequiredArgsConstructor
@CrossOrigin
public class BillController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final BillService billService;
    private final UserService userService;
    private final ProductService productService;

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
        // 0 : đang giao
        // 1 : đã giao thành công
        // 2 : chưa thanh toán
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
}
