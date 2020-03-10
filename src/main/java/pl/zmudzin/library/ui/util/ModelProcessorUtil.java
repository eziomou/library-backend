/*
 * MIT License
 *
 * Copyright (c) 2020 Piotr Żmudzin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package pl.zmudzin.library.ui.util;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Piotr Żmudzin
 */
public class ModelProcessorUtil {

    public static <T> ResponseEntity<EntityModel<T>> toResponseEntity(T t) {
        return toResponseEntity(t, HttpStatus.OK);
    }

    public static <T> ResponseEntity<EntityModel<T>> toResponseEntity(T t, HttpStatus status) {
        return new ResponseEntity<>(new EntityModel<>(t), status);
    }

    public static <T> ResponseEntity<PagedModel<EntityModel<T>>> toResponseEntity(Page<T> data) {
        return toResponseEntity(data, HttpStatus.OK);
    }

    private static <T> ResponseEntity<PagedModel<EntityModel<T>>> toResponseEntity(Page<T> data, HttpStatus status) {
        PagedModel<EntityModel<T>> model = toPagedModel(data);
        return new ResponseEntity<>(model, status);
    }

    private static <T> PagedModel<EntityModel<T>> toPagedModel(Page<T> page) {
        return new PagedModel<>(page.map(e -> new EntityModel<>(e)).getContent(), toPageMetadata(page));
    }

    private static <T> PagedModel.PageMetadata toPageMetadata(Page<T> page) {
        return new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
    }
}
