/*<license>
Copyright 2009 - $Date: 2008-10-15 18:22:22 +0200 (Wed, 15 Oct 2008) $ by PeopleWare n.v..

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package org.ppwcode.vernacular.l10n_III.util;

import java.util.Date;

public class BeanA {

  private String $string = null;
  private Date $date = null;
  private BeanA $bean = null;
  private Integer $integer = null;
  private Date $startDate = null;
  private Date $endDate = null;

  public BeanA() {
    // NOP
  }

  public String getString() {
    return $string;
  }

  public void setString(String string) {
    this.$string = string;
  }

  public Date getDate() {
    return $date;
  }

  public void setDate(Date date) {
    this.$date = date;
  }

  public BeanA getBean() {
    return $bean;
  }

  public void setBean(BeanA bean) {
    this.$bean = bean;
  }

  public Integer getInteger() {
    return $integer;
  }

  public void setInteger(Integer integer) {
    this.$integer = integer;
  }

  public Date getStartDate() {
    return $startDate;
  }

  public void setStartDate(Date startDate) {
    this.$startDate = startDate;
  }

  public Date getEndDate() {
    return $endDate;
  }

  public void setEndDate(Date endDate) {
    this.$endDate = endDate;
  }

}
