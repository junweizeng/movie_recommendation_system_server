package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zjw
 * @Classname UserPreferenceController
 * @Date 2022/4/20 16:14
 * @Description
 */
@RestController
@RequestMapping("/user/like")
public class UserPreferenceController {
    @Resource
    private RegionLikeService regionLikeService;

    @Resource
    private TypeLikeService typeLikeService;

    @Resource
    private UserService userService;

    @Resource
    private RecommendationService recommendationService;

    @GetMapping
    public Result<?> getTypesAndRegions(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Map<String, List<?>> typesAndRegions = userService.getTypesAndRegions(loginUser.getUser().getId());
        return Result.success(typesAndRegions);
    }

    @PostMapping("/update/types")
    public Result<?> updateUserTypeLike(@RequestBody int[] types, Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUser().getId();
        typeLikeService.updateUserTypeLike(id, types);
        recommendationService.updateRecommendation(id);
        return Result.success("电影类型喜好更新成功(‾◡◝)");
    }

    @PostMapping("/update/regions")
    public Result<?> updateUserRegionLike(@RequestBody int[] regions, Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUser().getId();
        regionLikeService.updateUserRegionLike(id, regions);
        recommendationService.updateRecommendation(id);
        return Result.success("电影地区喜好更新成功(‾◡◝)");
    }
}
