package com.example.order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LinYongJin
 * @date 2019/11/21 21:04
 */
public class OrderEntry {

    private Long id;

    private String name;

    private String desc;

    public OrderEntry(Long id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "OrderEntry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public static List<OrderEntry> getOrderList() {
        //创建 付款 完成 推送
        List<OrderEntry> orders = new ArrayList<>();
        OrderEntry orderEntry = new OrderEntry(32132L, "Sam", "创建");
        orders.add(orderEntry);
        orderEntry = new OrderEntry(33321L, "Jane", "创建");
        orders.add(orderEntry);
        orderEntry = new OrderEntry(33321L, "Jane", "付款");
        orders.add(orderEntry);
        orderEntry = new OrderEntry(34234L, "Rose", "创建");
        orders.add(orderEntry);
        orderEntry = new OrderEntry(34234L, "Rose", "付款");
        orders.add(orderEntry);
        orderEntry = new OrderEntry(34234L, "Rose", "完成");
        orders.add(orderEntry);
        orderEntry = new OrderEntry(34234L, "Rose", "推送");
        orders.add(orderEntry);
        return orders;
    }
}
