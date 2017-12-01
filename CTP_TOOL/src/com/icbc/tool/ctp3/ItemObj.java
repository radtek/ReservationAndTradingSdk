package com.icbc.tool.ctp3;

public class ItemObj
{
  private String itemId = "";
  private String itemName = "";
  private String itemUrl = "";
  private String firstOpOrJsp = "";

  public String getItemId() { return this.itemId; }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }
  public String getItemName() {
    return this.itemName;
  }
  public void setItemName(String itemName) {
    this.itemName = itemName;
  }
  public String getItemUrl() {
    return this.itemUrl;
  }
  public void setItemUrl(String itemUrl) {
    this.itemUrl = itemUrl;
  }
  public String getFirstOpOrJsp() {
    return this.firstOpOrJsp;
  }
  public void setFirstOpOrJsp(String firstOpOrJsp) {
    this.firstOpOrJsp = firstOpOrJsp;
  }
}