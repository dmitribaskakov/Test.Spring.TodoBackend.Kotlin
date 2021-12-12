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
Возможные значения по которым будем искать приоритет
 */
public class PrioritySearchValues {
    private String title;
    private String email;
}
