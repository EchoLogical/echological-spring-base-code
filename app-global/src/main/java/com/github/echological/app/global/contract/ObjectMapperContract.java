package com.github.echological.app.global.contract;

import java.util.List;

public interface ObjectMapperContract<I, O> {
    O map(I input);
    List<O> mapList(List<I> input);
}
