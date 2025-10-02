package com.github.echological.akint.global.contract;

import java.util.List;

public interface ObjectMapperContract<I, O> {
    O map(I input);
    List<O> mapList(List<I> input);
}
