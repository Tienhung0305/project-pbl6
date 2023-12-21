package com.example.sportstore06.controller;


import com.example.sportstore06.dao.request.ProductInfoRequest;
import com.example.sportstore06.dao.response.ProductInfoResponse;
import com.example.sportstore06.model.ProductInfo;
import com.example.sportstore06.model.Role;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/v1/product-information")
@RequiredArgsConstructor
public class ProductInfoController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final ProductInfoService productInfoService;
    private final ImageService imageService;
    private final BusinessService businessService;
    private final SaleService saleService;
    private final CategoryService categoryService;
    private final RoleService roleService;

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
                                    @RequestParam(value = "state", required = false) Optional<Integer> state,
                                    @RequestParam(value = "state_business", required = false) Optional<Integer> state_business) {
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
                if (state_business.isEmpty()) {
                    byPage = productInfoService.SearchByName(pageable, name, state.get());
                } else {
                    byPage = productInfoService.SearchByName(pageable, name, state.get(), state_business.get());
                }
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
                                        @RequestParam(value = "state", required = false) Optional<Integer> state,
                                        @RequestParam(value = "state_business", required = false) Optional<Integer> state_business) {


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
                if (state_business.isEmpty()) {
                    byPage = productInfoService.findByPage(pageable, state.get());
                } else {
                    byPage = productInfoService.findByPage(pageable, state.get(), state_business.get());
                }
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
                                            @RequestParam(value = "state", required = false) Optional<Integer> state,
                                            @RequestParam(value = "state_business", required = false) Optional<Integer> state_business) {
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
                if (state_business.isEmpty()) {
                    byPage = productInfoService.findByCategory(pageable, state.get(), set_id_category);
                } else {
                    byPage = productInfoService.findByCategory(pageable, state.get(), set_id_category, state_business.get());
                }
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
            Pageable pageable = null;
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


    @GetMapping("/search-with-business/{id_business}")
    public ResponseEntity<?> searchByBusiness(@PathVariable("id_business") Integer id_business,
                                              @RequestParam(value = "name", required = true) String name,
                                              @RequestParam(value = "page", required = false) Optional<Integer> page,
                                              @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                              @RequestParam(value = "sort", required = false) String sort,
                                              @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                              @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            if (businessService.findById(id_business).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
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
                byPage = productInfoService.SearchByNameAndBusiness(pageable, name, state.get(), id_business);
            } else {
                byPage = productInfoService.SearchByNameAndBusiness(pageable, name, id_business);
            }
            Page<ProductInfoResponse> responses = byPage.map(product -> product != null ? new ProductInfoResponse(product) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }


    @GetMapping("/search-with-sale/{id_sale}")
    public ResponseEntity<?> searchBySale(@PathVariable("id_sale") Integer id_sale,
                                          @RequestParam(value = "name", required = true) String name,
                                          @RequestParam(value = "page", required = false) Optional<Integer> page,
                                          @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                          @RequestParam(value = "state", required = false) Optional<Integer> state,
                                          @RequestParam(value = "state_business", required = false) Optional<Integer> state_business) {
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
                if (state_business.isEmpty()) {
                    byPage = productInfoService.SearchByNameAndSale(pageable, name, state.get(), id_sale);
                } else {
                    byPage = productInfoService.SearchByNameAndSale(pageable, name, state.get(), id_sale, state_business.get());
                }
            } else {
                byPage = productInfoService.SearchByNameAndSale(pageable, name, id_sale);
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
                                        @RequestParam(value = "state", required = false) Optional<Integer> state,
                                        @RequestParam(value = "state_business", required = false) Optional<Integer> state_business) {
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
                if (state_business.isEmpty()) {
                    byPage = productInfoService.findBySale(pageable, state.get(), id_sale);
                } else {
                    byPage = productInfoService.findBySale(pageable, state.get(), id_sale, state_business.get());
                }
            } else {
                byPage = productInfoService.findBySale(pageable, id_sale);
            }
            Page<ProductInfoResponse> responses = byPage.map(product -> product != null ? new ProductInfoResponse(product) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping("/find-by-sale-discount/{discount}")
    public ResponseEntity<?> findBySale(@PathVariable("discount") Double discount,
                                        @RequestParam(value = "page", required = false) Optional<Integer> page,
                                        @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                        @RequestParam(value = "sort", required = false) String sort,
                                        @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                        @RequestParam(value = "state", required = false) Optional<Integer> state,
                                        @RequestParam(value = "state_business", required = false) Optional<Integer> state_business) {
        try {
            if (discount < 1 || discount > 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("discount must be between 1 and 100");
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
                if (state_business.isEmpty()) {
                    byPage = productInfoService.findBySaleDiscount(pageable, state.get(), discount);
                } else {
                    byPage = productInfoService.findBySaleDiscount(pageable, state.get(), discount, state_business.get());
                }
            } else {
                byPage = productInfoService.findBySaleDiscount(pageable, discount);
            }
            Page<ProductInfoResponse> responses = byPage.map(product -> product != null ? new ProductInfoResponse(product) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }


    //end
    @PostMapping("/save")
    private ResponseEntity<?> addProduct(@Valid @RequestBody ProductInfoRequest request,
                                         @AuthenticationPrincipal User user) {
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
            if (request.getId_sale() != null && saleService.findById(request.getId_sale()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id sale not found");
            }
            Role role_admin = roleService.findByName("ROLE_ADMIN").get();
            if (!user.getRoleSet().contains(role_admin)) {
                if (user.getId() != request.getId_business()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you don't have the authority to edit");
                }
            }
            int id = productInfoService.save(0, request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(String.valueOf(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    private ResponseEntity<?> changeProduct(@Valid @RequestBody ProductInfoRequest request,
                                            @PathVariable("id") Integer id,
                                            @AuthenticationPrincipal User user) {
        try {
            if (productInfoService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found");
            }
            for (int id_image : request.getId_imageSet()) {
                if (imageService.findById(id_image).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
                }
            }
            if (request.getId_categorySet() != null) {
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
            Role role_admin = roleService.findByName("ROLE_ADMIN").get();
            if (!user.getRoleSet().contains(role_admin)) {
                if (user.getId() != request.getId_business()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you don't have the authority to edit");
                }
            }
            productInfoService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/change-state/{id}")
    private ResponseEntity<?> changeState(@PathVariable("id") Integer id,
                                          @RequestParam(value = "state", required = true) Integer state,
                                          @AuthenticationPrincipal User user) {
        try {
            if (productInfoService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found");
            } else if (state < 0 || state > 3) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("state not found");
            } else {
                if (productInfoService.findById(id).get().getState_before() == null) {
                    productInfoService.changeState(id, state);
                } else {
                    productInfoService.changeStateBefore(id, null);
                    productInfoService.changeState(id, state);
                }
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/hide-product-information/{id}")
    private ResponseEntity<?> hide(@PathVariable("id") Integer id,
                                   @RequestParam(value = "hide", required = true) Boolean hide,
                                   @AuthenticationPrincipal User user) {
        try {

            if (productInfoService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found");
            } else {
                //check role
                Role role_admin = roleService.findByName("ROLE_ADMIN").get();
                if (!user.getRoleSet().contains(role_admin)) {
                    if (user.getId() != productInfoService.findById(id).get().getBusiness().getId()) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you don't have the authority to edit");
                    }
                }

                if (hide) {
                    if (productInfoService.findById(id).get().getState() != 2) {
                        productInfoService.changeStateBefore(id, productInfoService.findById(id).get().getState());
                        productInfoService.changeState(id, 2);
                    }
                } else {
                    if (productInfoService.findById(id).get().getState_before() != null) {
                        Integer state_before = productInfoService.findById(id).get().getState_before();
                        productInfoService.changeState(id, state_before);
                    }
                }
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

    @PostMapping("/add-sale-product-information/{id_sale}")
    private ResponseEntity<?> addSaleProductInfo(
            @RequestParam(value = "set_id_product_inf", required = true) List<Integer> set_id_product_inf,
            @PathVariable(value = "id_sale", required = true) Integer id_sale) {
        try {
            if (saleService.findById(id_sale).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id sale not found");
            }
            for (Integer id : set_id_product_inf) {
                if (productInfoService.findById(id).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found");
                }else {
                    productInfoService.addSaleProductInfo(id, id_sale);
                }
            }
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}