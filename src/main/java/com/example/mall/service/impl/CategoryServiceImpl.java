package com.example.mall.service.impl;

import com.example.mall.dao.CategoryMapper;
import com.example.mall.pojo.Category;
import com.example.mall.service.ICategoryService;
import com.example.mall.vo.CategoryVo;
import com.example.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.mall.consts.MallConst.ROOT_PARENT_ID;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 耗时： http(请求微信API) -》 磁盘 -》Java程序（内存中）
     * mysql(内网+磁盘)
     * @return
     */

    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
      //  List<CategoryVo> categoryVoList = new ArrayList<>();
        List<Category> categories = categoryMapper.selectAll();
        //查出parent_id=0
//        for (Category category : categories){
//            if(category.getParentId().equals(ROOT_PARENT_ID)){
//                CategoryVo categoryVo = new CategoryVo();
//                BeanUtils.copyProperties(category,categoryVo);
//                categoryVoList.add(categoryVo);
//            }
//        }
        //lambda + stream
        List<CategoryVo> categoryVoList = categories.stream()
                .filter((e -> e.getParentId().equals(ROOT_PARENT_ID)))
                .map(e -> category2CategoryVo(e))
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())   // 根目录sort类型排序
                .collect(Collectors.toList());

        // 查询子目录
        findSubCategory(categoryVoList, categories);

        return ResponseVo.success(categoryVoList);
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id,resultSet,categories);
    }

    private void findSubCategoryId(Integer id, Set<Integer> resultSet,List<Category> categories){
        for (Category category : categories){
            if(category.getParentId().equals(id)){
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(),resultSet,categories);
            }
        }
    }

    //查询子目录
    private void findSubCategory(List<CategoryVo> categoryVoList, List<Category> categories){
        for(CategoryVo categoryVo : categoryVoList){
            List<CategoryVo> subCategoryVoList = new ArrayList<>();
            // categories 是数据库中所有的数据
            for(Category category : categories){
                // 如果查到内容， 需要设置子目录并继续往下查询（多级目录）
                if(categoryVo.getId().equals(category.getParentId())){
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }

                //做一下排序,为降序排列
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());

                categoryVo.setSubCategories(subCategoryVoList);   // 二级目录

                findSubCategory(subCategoryVoList, categories);     // 递归得到多级目录
            }
        }
    }


    private  CategoryVo category2CategoryVo(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
