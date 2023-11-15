package com.track.lol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.track.lol.domain.Hero;
import com.track.lol.domain.Skin;
import com.track.until.HttpClientUtil;

import java.util.List;

public class PictureLOL {

    public static void main(String[] args) {
        // https://game.gtimg.cn/images/lol/act/img/js/heroList/hero_list.js?ts=2832381
        String heroUrl = "https://game.gtimg.cn/images/lol/act/img/js/heroList/hero_list.js?ts=2832381";
        String pictureUrl = "https://game.gtimg.cn/images/lol/act/img/js/hero/";

        String heroStr = HttpClientUtil.doPost(heroUrl, "", "utf-8");
        JSONObject jsonHero =  JSON.parseObject(heroStr);
        List<Hero> heros = JSON.parseArray(String.valueOf(jsonHero.get("hero")), Hero.class);

        for (Hero hero : heros) {
            // todo 使用线程池

            String url = pictureUrl + hero.getHeroId() + ".js?ts=2832382";
            String skinStr = HttpClientUtil.doPost(url, "", "utf-8");
            System.out.println(url);
//            System.out.println(skinStr);
            JSONObject jsonSkin =  JSON.parseObject(skinStr);
            List<Skin> skins = JSON.parseArray(String.valueOf(jsonSkin.get("skins")), Skin.class);
            hero.setSkins(skins);

            for (Skin skin : skins){
                HttpClientUtil.downloadPicture(skin.getMainImg(),"D:\\test\\" + skin.getSkinId() + " " +skin.getName() + ".jpg");
            }


        }




//        System.out.println(s);

    }

}
