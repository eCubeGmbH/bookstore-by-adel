package com.example.demo.test;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class ParameterCallTest {

    @Test
    void call_by_value() {
        var byValue = 10L;

        method_by_value(byValue);

        assertThat(byValue).isEqualTo(10L);
    }

    @Test
    void call_by_reference_primitive_datatype() {

        var byReference = Long.valueOf(10L);
        method_by_reference(byReference);

        assertThat(byReference).isEqualTo(10L);
    }

    @Test
    void call_by_reference_object() {

        var list = new ArrayList<Long>();
        list.add(10L);
        list.add(20L);
        list.add(30L);

        assertThat(list).hasSize(3);

        methode_add_element(list);

        assertThat(list).hasSize(4);
    }

    @Test
    void call_by_reference_object2() {

        var list = new ArrayList<Long>();
        list.add(10L);
        list.add(20L);
        list.add(30L);

        assertThat(list).containsExactly(10L, 20L, 30L);

        methode_change_values(list);

        assertThat(list).containsExactly(20L, 30L, 40L);
    }

    private void method_by_value(long value) {
        value += 10;
    }

    private void method_by_reference(Long value) {
        value += 10;
    }

    private void methode_add_element(List<Long> myList) {
        myList.add(40L);
    }

    private void methode_change_values(List<Long> myList) {
        myList.clear();
        myList.add(20L);
        myList.add(30L);
        myList.add(40L);
    }
}
