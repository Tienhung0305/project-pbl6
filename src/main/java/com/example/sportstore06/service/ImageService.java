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
    public Long getCount() {
        return iImageRepository.count();
    }
    public Optional<Image> findById(Integer id) {
        return iImageRepository.findById(id);
    }
    public List<Image> SearchByName(String name) {
        return iImageRepository.SearchByName(name);
    }
    public Page<Image> findByPage(Pageable pageable) {
        return iImageRepository.findByPage(pageable);
    }
    public boolean deleteById(Integer id) {
        try {
            iImageRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Integer save(Integer id, ImageRequest request) {
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
                is_main(request.getIs_main()).
                created_at(created_at).
                updated_at(updated_at).
                url(request.getUrl()).
                build();
        iImageRepository.save(u);
        return u.getId();
    }

    public void change_url(Integer id, String url)
    {
        if(iImageRepository.findById(id).isPresent())
        {
            Image image = iImageRepository.findById(id).get();
            image.setUrl(url);
            iImageRepository.save(image);
        }
    }
}
