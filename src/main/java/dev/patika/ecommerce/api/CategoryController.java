package dev.patika.ecommerce.api;

import dev.patika.ecommerce.business.abstracts.ICategoryService;
import dev.patika.ecommerce.core.config.modelMapper.IModelMapperService;
import dev.patika.ecommerce.core.result.Result;
import dev.patika.ecommerce.core.result.ResultData;
import dev.patika.ecommerce.core.utilies.ResultHelper;
import dev.patika.ecommerce.dto.request.category.CategorySaveRequest;
import dev.patika.ecommerce.dto.request.category.CategoryUpdateRequest;
import dev.patika.ecommerce.dto.response.CursorResponse;
import dev.patika.ecommerce.dto.response.category.CategoryResponse;
import dev.patika.ecommerce.entities.Category;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {
    private final ICategoryService iCategoryService;
    private final IModelMapperService modelMapper;


    public CategoryController(ICategoryService iCategoryService, IModelMapperService modelMapper) {
        this.iCategoryService = iCategoryService;
        this.modelMapper = modelMapper;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<CategoryResponse> save(@Valid @RequestBody CategorySaveRequest categorySaveRequest){
        System.out.println(categorySaveRequest.toString());
        Category saveCategory = this.modelMapper.forRequest().map(categorySaveRequest,Category.class);
        this.iCategoryService.save(saveCategory);
        System.out.println(saveCategory.toString());
       return ResultHelper.created(this.modelMapper.forResponse().map(saveCategory,CategoryResponse.class));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CategoryResponse> get(@PathVariable("id") int id){
        Category category = this.iCategoryService.get(id);
        CategoryResponse categoryResponse = this.modelMapper.forResponse().map(category,CategoryResponse.class);
        return ResultHelper.success(categoryResponse);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<CategoryResponse>> cursor(
            @RequestParam(name = "page",required = false,defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ){
      Page<Category> categoryPage = this.iCategoryService.cursor(page,pageSize);
      Page<CategoryResponse> categoryResponsePage = categoryPage
              .map(category -> this.modelMapper.forResponse().map(category,CategoryResponse.class));
      return ResultHelper.cursor(categoryResponsePage);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CategoryResponse> update(@Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest){
        Category updateCategory = this.modelMapper.forRequest().map(categoryUpdateRequest,Category.class);
        this.iCategoryService.update(updateCategory);
        return ResultHelper.success(this.modelMapper.forResponse().map(updateCategory,CategoryResponse.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id){
        this.iCategoryService.delete(id);
        return ResultHelper.ok();
    }



}
