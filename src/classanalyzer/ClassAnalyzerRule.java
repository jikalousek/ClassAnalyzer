/*
 * Copyright (C) 2018 Jiri Kalousek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package classanalyzer;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * This class represents a rule that is connected with the ClassAnalyzer.
 * Every time any single JUnit test is started, this class defines what actions
 * have to happen at the beginning and at the end. ClassAnalyzer needs to be
 * initialized at the beginning and finished at the end.
 *
 * @author Jiri Kalousek
 * @param <I> Interface defining methods from tested class.
 */
public class ClassAnalyzerRule<I> extends TestWatcher {

    private final ClassAnalyzer<I> analyzer;

    public ClassAnalyzerRule(ClassAnalyzer<I> analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    protected void failed(Throwable e, Description description) {
        analyzer.closeInstrumentation();
    }

    @Override
    protected void succeeded(Description description) {
        analyzer.closeInstrumentation();
    }

    @Override
    protected void starting(Description description) {
    }

}
