package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.service.RegionLikeService;
import cn.zjw.mrs.service.TypeLikeService;
import cn.zjw.mrs.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zjw
 * @Classname UserLikeController
 * @Date 2022/4/20 16:14
 * @Description
 */
@RestController
@RequestMapping("/user/like")
public class UserLikeController {
    @Resource
    private RegionLikeService regionLikeService;

    @Resource
    private TypeLikeService typeLikeService;

    @Resource
    private UserService userService;

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
        int update = typeLikeService.updateUserTypeLike(id, types);
        if (update == 0) {
            return Result.success("电影类型喜好更新失败(┬┬﹏┬┬)");
        }
        return Result.success("电影类型喜好更新成功(‾◡◝)");
    }

    @PostMapping("/update/regions")
    public Result<?> updateUserRegionLike(@RequestBody int[] regions, Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUser().getId();
        int update = regionLikeService.updateUserRegionLike(id, regions);
        if (update == 0) {
            return Result.success("电影地区喜好更新失败(┬┬﹏┬┬)");
        }
        return Result.success("电影地区喜好更新成功(‾◡◝)");
    }
}
