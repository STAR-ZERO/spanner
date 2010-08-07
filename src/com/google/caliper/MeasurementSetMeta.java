/*
 * Copyright (C) 2010 Google Inc.
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
 */

package com.google.caliper;

import java.io.Serializable;

@SuppressWarnings("serial")
public final class MeasurementSetMeta
    implements Serializable /* for GWT Serialization */ {

  private /*final*/ MeasurementSet measurementSet;
  private /*final*/ String eventLog;

  public MeasurementSetMeta(MeasurementSet measurementSet, String eventLog) {
    this.measurementSet = measurementSet;
    this.eventLog = eventLog;
  }

  public MeasurementSet getMeasurementSet() {
    return measurementSet;
  }

  public String getEventLog() {
    return eventLog;
  }

  private MeasurementSetMeta() {} // for GWT Serialization
}