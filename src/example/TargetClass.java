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
package example;

/**
 * Example of class that can be tested with a use of JUnit testing and
 * ClassAnalyzer (code coverage and subsequent evaluation).
 *
 * @author Jiri Kalousek
 */
public class TargetClass implements ITargetClass {

    @Override
    public int max(final int a, final int b) {
        if (a >= b) {
            return a;
        } else {
            return b;
        }
    }

}
