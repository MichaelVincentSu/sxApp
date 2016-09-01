package com.sx.yufs.sxapp.webapi.okhttp.para;

import java.io.Serializable;
import java.util.List;

/**
 * 获取面料列表
 * Created by yufs on 2016/8/30.
 */
public class FablicList extends ResponseSu {

    public int total;

    public List<Fabric> Fabrics;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Fabric> getFabrics() {
        return Fabrics;
    }

    public void setFabrics(List<Fabric> fabrics) {
        Fabrics = fabrics;
    }

    /**
     * 面料信息
     */
    public static class Fabric implements Serializable {
        /**
         * 面料名称
         */
        public String FabricName;

        /**
         * 面料代码
         */
        public String FabricCode;

        /**
         * 图片路径
         */
        public String PictureUrl;
        /**
         * 大图片路径
         */
        public String ThumbnailUrl;
        /**
         * id
         */
        public int Id;
    }
}
