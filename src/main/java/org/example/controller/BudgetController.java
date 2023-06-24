package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.entity.Budget;
import org.example.service.BudgetService;
import org.example.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public R<List> getBudget(HttpServletRequest request){
//        Long userid  = Long.valueOf(request.getSession().getAttribute("user").toString());
        if(request.getSession().getAttribute("userid")!=null){
            Long userid  = Long.valueOf(request.getSession().getAttribute("userid").toString());
            LambdaQueryWrapper<Budget> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Budget::getUserid,userid);
            queryWrapper.eq(Budget::getIsdelete,0);

            return R.success(budgetService.list(queryWrapper));
        }
        else{
            return R.error("查询失败");
        }


    }

    @PostMapping("/add")
    public R<Budget> addBudget(HttpServletRequest request, @RequestBody Budget budget){

        budget.setIsdelete(0);
        budget.setBudgetid(IdUtil.IdCreate());
        budgetService.save(budget);

        return R.success(budget);
    }

    @DeleteMapping
    public R<String> deleteBudget(HttpServletRequest request,Long  budgetid){
        Budget budget = budgetService.getById(budgetid);

        budget.setIsdelete(1);
        budgetService.updateById(budget);
        return R.success("删除成功");
    }

    @PostMapping("/update")
    public R<String> updateBudge(HttpServletRequest request,@RequestBody Budget budget){
        budgetService.updateById(budget);
        return R.success("/修改成功");
    }

}
