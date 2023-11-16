package com.example.sportstore06.controller;


import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.dao.response.ProductResponse;
import com.example.sportstore06.dao.response.UserResponse;
import com.example.sportstore06.model.Product;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin()
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final ProductService productService;
    private final ImageService imageService;
    private final BusinessService businessService;
    private final SaleService saleService;
    private final CategoryService categoryService;

    @GetMapping("/get-count")
    public ResponseEntity<?> getCount() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getCount());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            if (productService.findById(id).isPresent()) {
                ProductResponse p = new ProductResponse(productService.findById(id).get());
                return ResponseEntity.status(HttpStatus.OK).body(p);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "name", required = true) String name,
                                    @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            List<Product> list = new ArrayList<Product>();
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                list = productService.SearchByName(name, state.get());
            } else {
                list = productService.SearchByName(name);
            }
            List<ProductResponse> response = list.stream().map(product -> new ProductResponse(product)).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
            Page<Product> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = productService.findByPage(pageable, state.get());
            } else {
                byPage = productService.findByPage(pageable);
            }
            Page<ProductResponse> responses = byPage.map(product -> new ProductResponse(product));
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    //update
    @GetMapping("/find-by-category/{id_category}")
    public ResponseEntity<?> findByCategory(@PathVariable("id_category") Integer id_category,
                                            @RequestParam(value = "page", required = false) Optional<Integer> page,
                                            @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                            @RequestParam(value = "sort", required = false) String sort,
                                            @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                            @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            if (categoryService.findById(id_category).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id category not found");
            }
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Product> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = productService.findByCategory(pageable, state.get(), id_category);
            } else {
                byPage = productService.findByCategory(pageable, id_category);
            }
            Page<ProductResponse> responses = byPage.map(product -> new ProductResponse(product));
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
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Product> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = productService.findByBusiness(pageable, state.get(), id_business);
            } else {
                byPage = productService.findByBusiness(pageable, id_business);
            }
            Page<ProductResponse> responses = byPage.map(product -> new ProductResponse(product));
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
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Product> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = productService.findBySale(pageable, state.get(), id_sale);
            } else {
                byPage = productService.findBySale(pageable, id_sale);
            }
            Page<ProductResponse> responses = byPage.map(product -> new ProductResponse(product));
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping("/find-by-brand/{brand}")
    public ResponseEntity<?> findByBrand(@PathVariable("brand") String brand,
                                         @RequestParam(value = "page", required = false) Optional<Integer> page,
                                         @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                         @RequestParam(value = "sort", required = false) String sort,
                                         @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                         @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            if (productService.findByBrand(brand).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("brand not found");
            }
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Product> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = productService.findByBrand(pageable, state.get(), brand);
            } else {
                byPage = productService.findByBrand(pageable, brand);
            }
            Page<ProductResponse> responses = byPage.map(product -> new ProductResponse(product));
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    //end

    @PostMapping("/save")
    private ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest request) {
        try {
            for (int id_image : request.getId_imageSet()) {
                if (imageService.findById(id_image).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
                }
            }
            if (businessService.findById(request.getId_business()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
            }
            if (request.getId_sale() != null && saleService.findById(request.getId_business()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id sale not found");
            }
            if (request.getId_category() != null && categoryService.findById(request.getId_category()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id category not found");
            }
            productService.save(0, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    private ResponseEntity<?> changeProduct(@Valid @RequestBody ProductRequest request,
                                            @PathVariable("id") Integer id) {
        try {
            if (productService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            }
            for (int id_image : request.getId_imageSet()) {
                if (imageService.findById(id_image).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
                }
            }
            if (businessService.findById(request.getId_business()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
            }
            if (request.getId_sale() != null && saleService.findById(request.getId_business()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id sale not found");
            }
            if (request.getId_category() != null && categoryService.findById(request.getId_category()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id category not found");
            }
            productService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/change-state/{id}")
    private ResponseEntity<?> changeState(@PathVariable("id") Integer id,
                                          @RequestParam(value = "state", required = true) Integer state) {
        try {
            if (productService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            } else if (state < 0 || state > 3) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("state not found");
            } else {
                productService.changeState(id, state);
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        try {
            if (productService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            } else {
                boolean checkDelete = productService.deleteById(id);
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