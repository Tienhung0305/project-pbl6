package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.SaleRequest;
import com.example.sportstore06.dao.response.SaleResponse;
import com.example.sportstore06.dao.response.UserResponse;
import com.example.sportstore06.model.Sale;
import com.example.sportstore06.model.User;
import com.example.sportstore06.service.*;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sale")
@RequiredArgsConstructor
@CrossOrigin()
public class SaleController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final SaleService saleService;
    private final BusinessService businessService;

    @GetMapping("/get-count")
    public ResponseEntity<?> getCount() {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getCount());
    }

//    @GetMapping("/get-discount")
//    public ResponseEntity<?> getDiscount() {
//        Double discount_max = saleService.getMaxDiscount();
//        Double discount_min = saleService.getMinDiscount();
//        Map<String, Double> result = new HashMap<>();
//        result.put("discount_max", discount_max);
//        result.put("discount_min", discount_min);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            if (saleService.findById(id).isPresent()) {
                SaleResponse s = new SaleResponse(saleService.findById(id).get());
                return ResponseEntity.status(HttpStatus.OK).body(s);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id sale not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-by-business{id_business}")
    public ResponseEntity<?> findByIdBusiness(
            @PathVariable(value = "id_business", required = true) Integer id_business,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "desc", required = false) Optional<Boolean> desc) {
        try {
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            if (businessService.findById(id_business).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
            }
            Page<Sale> byPage = saleService.findByIdBusiness(pageable, id_business);
            Page<SaleResponse> responses = byPage.map(sale -> sale != null ? new SaleResponse(sale) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping("/get-by-discount")
    public ResponseEntity<?> findByIdBusiness(
            @RequestParam(value = "discount_min", required = true) Double discount_min,
            @RequestParam(value = "discount_max", required = true) Double discount_max,
            @RequestParam(value = "id_business", required = false) Optional<Integer> id_business,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "desc", required = false) Optional<Boolean> desc) {
        try {
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Sale> byPage;
            if (id_business.isEmpty()) {
                byPage = saleService.getByDiscount(pageable, discount_min, discount_max);
            } else {
                if (businessService.findById(id_business.get()).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
                }
                byPage = saleService.getByDiscount(pageable, discount_min, discount_max, id_business.get());
            }
            Page<SaleResponse> responses = byPage.map(sale -> sale != null ? new SaleResponse(sale) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "name", required = true) String name,
                                    @RequestParam(value = "page", required = false) Optional<Integer> page,
                                    @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                    @RequestParam(value = "sort", required = false) String sort,
                                    @RequestParam(value = "desc", required = false) Optional<Boolean> desc) {
        try {
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Sale> byPage = saleService.SearchByName(pageable, name);
            Page<SaleResponse> responses = byPage.map(sale -> sale != null ? new SaleResponse(sale) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping()
    public ResponseEntity<?> findByPage(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                        @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                        @RequestParam(value = "sort", required = false) String sort,
                                        @RequestParam(value = "desc", required = false) Optional<Boolean> desc) {
        try {
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Sale> byPage = saleService.findByPage(pageable);
            Page<SaleResponse> responses = byPage.map(sale -> sale != null ? new SaleResponse(sale) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @PostMapping("/save")
    private ResponseEntity<?> addSale(@Valid @RequestBody SaleRequest request) {
        try {
            if (businessService.findById(request.getId_business()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
            }
            saleService.save(0, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    private ResponseEntity<?> changeSale(@Valid @RequestBody SaleRequest request,
                                         @PathVariable("id") Integer id) {
        try {
            if (saleService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id sale not found");
            }
            if (businessService.findById(request.getId_business()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
            }
            saleService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        try {
            if (saleService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id sale not found");
            } else {
                boolean checkDelete = saleService.deleteById(id);
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
