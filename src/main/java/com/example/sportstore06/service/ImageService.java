package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.ImageRequest;
import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.model.Business;
import com.example.sportstore06.model.Image;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.repository.ICategoryRepository;
import com.example.sportstore06.repository.IImageRepository;
import com.example.sportstore06.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final IImageRepository iImageRepository;
    public Optional<Image> findById(int id) {
        return iImageRepository.findById(id);
    }
    public List<Image> findByName(String name) {
        return iImageRepository.findByName(name);
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

    public void save(int id, ImageRequest request) {
        Timestamp created_at;
        Timestamp updated_at;
        if (iImageRepository.findById(id).isPresent()) {
            created_at = iImageRepository.findById(id).get().getCreated_at();
            updated_at = new Timestamp(new Date().getTime());
        } else {
            created_at = new Timestamp(new Date().getTime());
            updated_at = created_at;
        }
        var u = Image.builder().
                id(id).
                name(request.getName()).
                created_at(created_at).
                updated_at(updated_at).
                url(request.getUrl()).
                build();
        iImageRepository.save(u);
    }

    public void change_url(int id, String url)
    {
        if(iImageRepository.findById(id).isPresent())
        {
            Image image = iImageRepository.findById(id).get();
            image.setUrl(url);
            iImageRepository.save(image);
        }
    }
}
