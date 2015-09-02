/*
 * Copyright 2010, 2014 Jan Ouwens
 *
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
package nl.jqno.equalsverifier.integration.extended_contract;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class DontOverrideEqualsTest {
    @Test
    public void succeed_whenClassDoesntOverrideEqualsOrHashCode() {
        EqualsVerifier.forClass(Pojo.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .allFieldsShouldBeUsed()
                .verify();
    }

    public final class Pojo {
        private String value;

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return getClass().getName() + " " + value;
        }
    }
}
