package org.home.todobackend.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
/*
Возможные значения по которым будем искать категорию
 */
public class CategorySearchValues {
    private String title;
    private String email;
}
