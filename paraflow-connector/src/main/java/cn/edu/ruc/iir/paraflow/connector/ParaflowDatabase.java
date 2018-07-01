/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.ruc.iir.paraflow.connector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

/**
 * @author jelly.guodong.jin@gmail.com
 */
public class ParaflowDatabase
{
    private final String name;
    private String location;
    private String id;

    @JsonCreator
    public ParaflowDatabase(
            @JsonProperty("name") String name)
    {
        this.name = requireNonNull(name, "name is null");
        this.location = "";
    }

    @JsonProperty
    public String getName()
    {
        return name;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    @JsonProperty
    public String getLocation()
    {
        return location;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @JsonProperty
    public String getId()
    {
        return id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ParaflowDatabase other = (ParaflowDatabase) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("name", name)
                .toString();
    }
}
