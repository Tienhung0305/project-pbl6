package com.example.sportstore06.controller;


import com.example.sportstore06.dao.request.ProductInfoRequest;
import com.example.sportstore06.dao.response.ProductInfoResponse;
import com.example.sportstore06.model.ProductInfo;
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

import java.util.*;


@RestController
@RequestMapping("/api/v1/product-information")
@RequiredArgsConstructor
@CrossOrigin()
public class ProductInfoController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final ProductInfoService productInfoService;
    private final ImageService imageService;
    private final BusinessService businessService;
    private final SaleService saleService;
    private final CategoryService categoryService;

    @GetMapping("/get-count")
    public ResponseEntity<?> getCount() {
        return ResponseEntity.status(HttpStatus.OK).body(productInfoService.getCount());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            if (productInfoService.findById(id).isPresent()) {
                ProductInfoResponse p = new ProductInfoResponse(productInfoService.findById(id).get());
                return ResponseEntity.status(HttpStatus.OK).body(p);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found");
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
            Page<ProductInfo> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = productInfoService.SearchByName(pageable, name, state.get());
            } else {
                byPage = productInfoService.SearchByName(pageable, name);
            }
            Page<ProductInfoResponse> responses = byPage.map(product -> product != null ? new ProductInfoResponse(product) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping
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
            Page<ProductInfo> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = productInfoService.findByPage(pageable, state.get());
            } else {
                byPage = productInfoService.findByPage(pageable);
            }
            Page<ProductInfoResponse> responses = byPage.map(product -> product != null ? new ProductInfoResponse(product) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    //update
    @GetMapping("/find-by-category/{set_id_category}")
    public ResponseEntity<?> findByCategory(@PathVariable("set_id_category") List<Integer> set_id_category,
                                            @RequestParam(value = "page", required = false) Optional<Integer> page,
                                            @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                            @RequestParam(value = "sort", required = false) String sort,
                                            @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                            @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            for (Integer id : set_id_category) {
                if (categoryService.findById(id).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id category not found");
                }
            }
            Pageable pageable = null;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<ProductInfo> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = productInfoService.findByCategory(pageable, state.get(), set_id_category);
            } else {
                byPage = productInfoService.findByCategory(pageable, set_id_category);
            }
            Page<ProductInfoResponse> responses = byPage.map(product -> product != null ? new ProductInfoResponse(product) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping("/find-by-business/{id_business}")
    public ResponseEntity<?> findByBusiness(@PathVariable("id_business") Integer id_business,
                                            @RequestParam(value = "page", required = false) Optional<Integer> page,
                                            @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                            @RequestParam(value = "sort", required = false) String sort,
                                            @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                            @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            if (businessService.findById(id_business).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
            }
            Pageable pageable= null;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<ProductInfo> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = productInfoService.findByBusiness(pageable, state.get(), id_business);
            } else {
                byPage = productInfoService.findByBusiness(pageable, id_business);
            }
            Page<ProductInfoResponse> responses = byPage.map(product -> product != null ? new ProductInfoResponse(product) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping("/find-by-sale/{id_sale}")
    public ResponseEntity<?> findBySale(@PathVariable("id_sale") Integer id_sale,
                                        @RequestParam(value = "page", required = false) Optional<Integer> page,
                                        @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                        @RequestParam(value = "sort", required = false) String sort,
                                        @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                        @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            if (saleService.findById(id_sale).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id sale not found");
            }
            Pageable pageable = null;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<ProductInfo> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = productInfoService.findBySale(pageable, state.get(), id_sale);
            } else {
                byPage = productInfoService.findBySale(pageable, id_sale);
            }
            Page<ProductInfoResponse> responses = byPage.map(product -> product != null ? new ProductInfoResponse(product) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }


    //end

    @PostMapping("/save")
    private ResponseEntity<?> addProduct(@Valid @RequestBody ProductInfoRequest request) {
        try {
            for (Integer id_image : request.getId_imageSet()) {
                if (imageService.findById(id_image).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
                }
            }

            for (Integer id_category : request.getId_categorySet()) {
                if (categoryService.findById(id_category).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id category not found");
                }
            }

            if (businessService.findById(request.getId_business()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
            }
            if (request.getId_sale() != null && saleService.findById(request.getId_business()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id sale not found");
            }

            productInfoService.save(0, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    private ResponseEntity<?> changeProduct(@Valid @RequestBody ProductInfoRequest request,
                                            @PathVariable("id") Integer id) {
        try {
            if (productInfoService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found");
            }
            for (int id_image : request.getId_imageSet()) {
                if (imageService.findById(id_image).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
                }
            }
            if(request.getId_categorySet() != null) {
                for (Integer id_category : request.getId_categorySet()) {
                    if (categoryService.findById(id_category).isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id category not found");
                    }
                }
            }
            if (businessService.findById(request.getId_business()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
            }
            if (request.getId_sale() != null && saleService.findById(request.getId_business()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id sale not found");
            }
            productInfoService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/change-state/{id}")
    private ResponseEntity<?> changeState(@PathVariable("id") Integer id,
                                          @RequestParam(value = "state", required = true) Integer state) {
        try {
            if (productInfoService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found");
            } else if (state < 0 || state > 3) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("state not found");
            } else {
                productInfoService.changeState(id, state);
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        try {
            if (productInfoService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found");
            } else {
                boolean checkDelete = productInfoService.deleteById(id);
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