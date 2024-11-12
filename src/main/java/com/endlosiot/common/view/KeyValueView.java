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
 * This is Key value view which used to send key value of enums in json format.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */
public class KeyValueView implements Comparable<KeyValueView>, View {

    private static final long serialVersionUID = -4330905917750411429L;

    private String value;
    private long key;

    public static final Comparator<KeyValueView> idComparator = new Comparator<KeyValueView>() {
        @Override
        public int compare(KeyValueView o1, KeyValueView o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    };

    public static final Comparator<KeyValueView> nameComparator = new Comparator<KeyValueView>() {
        @Override
        public int compare(KeyValueView o1, KeyValueView o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    };

    public KeyValueView() {
        super();
    }

    public KeyValueView(long key, String value) {
        this.key = key;
        this.value = value;
    }

    public static KeyValueView create(long key, String value) {
        return new KeyValueView(key, value);
    }

    public static List<Long> getKeyFromKeyValuePair(List<KeyValueView> keyValueView) {
        List<Long> keys = new ArrayList<>();
        for (KeyValueView temp : keyValueView) {
            keys.add(temp.getKey());
        }
        return keys;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Long getKey() {
        return key;
    }

    public void setId(int key) {
        this.key = key;
    }

    @Override
    public int compareTo(KeyValueView that) {
        return value.compareTo(that.getValue());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getKey() == 0L) ? 0 : getKey().hashCode());
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
        KeyValueView other = (KeyValueView) obj;
        if (getKey() == null) {
            if (other.getKey() != null) {
                return false;
            }
        } else if (!getKey().equals(other.getKey())) {
            return false;
        }
        return true;
    }
}
