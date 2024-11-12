/*******************************************************************************
 * Copyright -2019 @intentlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.endlosiot.common.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This is id name view which used to send id name of enums in json format.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */
public class IdNameView implements Comparable<IdNameView>, View {

    private static final long serialVersionUID = -721096460835161596L;

    private String name;
    private long id;

    public static final Comparator<IdNameView> idComparator = new Comparator<IdNameView>() {
        @Override
        public int compare(IdNameView o1, IdNameView o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };

    public static final Comparator<IdNameView> nameComparator = new Comparator<IdNameView>() {
        @Override
        public int compare(IdNameView o1, IdNameView o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public IdNameView() {
        super();
    }

    public IdNameView(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static IdNameView create(long key, String value) {
        return new IdNameView(key, value);
    }

    public static List<Long> getKeyFromKeyValuePair(List<IdNameView> keyValueView) {
        List<Long> keys = new ArrayList<>();
        for (IdNameView temp : keyValueView) {
            keys.add(temp.getId());
        }
        return keys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int compareTo(IdNameView that) {
        return name.compareTo(that.getName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == 0L) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        IdNameView other = (IdNameView) obj;
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
    }
}
