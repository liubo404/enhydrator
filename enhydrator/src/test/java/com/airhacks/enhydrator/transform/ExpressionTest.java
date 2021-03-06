package com.airhacks.enhydrator.transform;

/*
 * #%L
 * enhydrator
 * %%
 * Copyright (C) 2014 Adam Bien
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import com.airhacks.enhydrator.in.Column;
import com.airhacks.enhydrator.in.Row;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class ExpressionTest {

    Expression cut;

    @Before
    public void init() {
        this.cut = new Expression();
    }

    @Test
    public void bindingsAreWorking() {
        String expression = "print($ROW); print($ROW.numberOfColumns);$ROW";
        Row row = new Row();
        row.addColumn(-1, "chief", "duke");
        row.addColumn(-1, "master", "juggy");
        Row execute = this.cut.execute(row, expression);
        assertFalse(execute.isEmpty());
    }

    @Test
    public void emptyList() {
        Row row = new Row();
        row.addColumn(-1, "chief", "duke");
        Row execute = this.cut.execute(row, "$EMPTY");
        assertTrue(execute.isEmpty());
    }

    @Test
    public void emptyExpression() {
        Row row = new Row();
        row.addColumn(-1, "chief", "duke");
        Row result = this.cut.execute(row, "");
        assertThat(result, is(row));
    }

    @Test
    public void accessToColumn() {
        Row row = new Row();
        final String inputValue = "duke";
        row.addColumn(-1, "chief", inputValue);
        Row result = this.cut.execute(row, "$EMPTY.addColumn(-1,'new_duke',chief.value + '_modified');$EMPTY;");
        Column column = result.getColumnByName("new_duke");
        assertNotNull(column);
        Object value = column.getValue();
        assertNotNull(value);
        assertThat(value, is(inputValue + "_modified"));
    }

}
