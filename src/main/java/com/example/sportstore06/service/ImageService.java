package com.example.sportstore06.service;

import com.example.sportstore06.model.Image;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.repository.IImageRepository;
import com.example.sportstore06.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final IImageRepository iImageRepository;

    public Optional<Image> findById(int id) {
        return iImageRepository.findById(id);
    }

    public Page<Image> findByPage(Pageable pageable) {
        return iImageRepository.findByPage(pageable);
    }

    public boolean deleteById(int id) {
        try {
            iImageRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
