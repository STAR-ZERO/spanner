/*
 * Copyright (C) 2012 Google Inc.
 * Copyright (C) 2015 Christian Melchior
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Original author: gak@google.com (Gregory Kick)
 */

package dk.ilios.spanner.model;

import com.google.common.base.Objects;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A single, weighted measurement.
 */
public class Measurement implements Serializable {
    private static final long serialVersionUID = 1L;

    private Value value;
    private double weight;
    private String description;

    private Measurement(Builder builder) {
        this.value = builder.value;
        this.description = builder.description;
        this.weight = builder.weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Measurement) {
            Measurement that = (Measurement) obj;
            return this.value.equals(that.value)
                    && this.weight == that.weight
                    && this.description.equals(that.description);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, weight, description);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("value", value)
                .add("weight", weight)
                .add("description", description)
                .toString();
    }

    public Value value() {
        return value;
    }

    public double weight() {
        return weight;
    }

    public String description() {
        return description;
    }

    public static final class Builder {
        private Value value;
        private Double weight;
        private String description;

        /**
         * The measurement value.
         */
        public Builder value(Value value) {
            this.value = checkNotNull(value);
            return this;
        }

        /**
         * The weight of the value. Normally this is 1, but for a test that internally does repetitions
         * to get above the timer granularity, the weight is the number repetitions done internally by
         * the benchmark method.
         */
        public Builder weight(double weight) {
            checkArgument(weight > 0);
            this.weight = weight;
            return this;
        }

        /**
         * A description of what is being measured (if needed).
         */
        public Builder description(String description) {
            this.description = checkNotNull(description);
            return this;
        }

        public Measurement build() {
            checkArgument(value != null);
            checkArgument(weight != null);
            checkArgument(description != null);
            return new Measurement(this);
        }
    }
}
