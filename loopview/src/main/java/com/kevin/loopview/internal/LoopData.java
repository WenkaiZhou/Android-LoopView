/*
 * Copyright (c) 2018 Kevin zhou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevin.loopview.internal;

import java.util.List;

/**
 * LoopData
 *
 * @author zwenkai@foxmail.com, Created on 2015-1-14 19:30:18
 *         Major Function：<b>自定义控件可以自动跳动的ViewPager数据实体</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */

public class LoopData {

    /**
     * 每个条目数据
     */
    public List<ItemData> items;

    public class ItemData {
        /**
         * 图片地址
         */
        public String img;
        /**
         * 描述信息
         */
        public String desc;
        /**
         * 链接操作
         */
        public String link;

        public ItemData() {
        }

        public ItemData(String img, String desc, String link) {
            this.img = img;
            this.desc = desc;
            this.link = link;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (other instanceof ItemData) {
                ItemData itemData = (ItemData) other;
                if (null == itemData.img) {
                    return null == img;
                }
                if (null == itemData.desc) {
                    return null == desc;
                }
                if (null == itemData.link) {
                    return null == link;
                }
                return itemData.img.equals(img)
                        && itemData.desc.equals(desc)
                        && itemData.link.equals(link);
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof LoopData) {
            LoopData loopData = (LoopData) obj;
            if (null != loopData.items
                    && null != loopData
                    && loopData.items.size() == items.size()) {
                for (int i = 0; i < items.size(); i++) {
                    if (!items.get(i).equals(loopData.items.get(i))) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}