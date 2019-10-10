/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package ai.djl.training.loss;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.training.Activation;

public class HingeLoss extends Loss {

    private int margin;
    private float weight;
    private int batchAxis;

    /**
     * Calculate Hinge loss.
     *
     * <p>.. math:: L = \sum_i max(0, {margin} - {pred}_i \cdot {label}_i)
     *
     * @param margin The margin in hinge loss. Defaults to 1.0
     * @param weight weight to apply on loss value, default 1
     * @param batchAxis axis that represents mini-batch, default 0
     */
    public HingeLoss(int margin, float weight, int batchAxis) {
        this.margin = margin;
        this.weight = weight;
        this.batchAxis = batchAxis;
    }

    /**
     * Calculate Hinge loss.
     *
     * <p>.. math:: L = \sum_i max(0, {margin} - {pred}_i \cdot {label}_i)
     */
    public HingeLoss() {
        margin = 1;
        weight = 1;
        batchAxis = 0;
    }

    /** {@inheritDoc} */
    @Override
    public NDArray getLoss(NDArray label, NDArray prediction) {
        label = label.reshape(prediction.getShape());
        NDArray loss = Activation.relu(NDArrays.sub(margin, label.mul(prediction)));
        if (weight != 1) {
            loss = loss.mul(weight);
        }
        return loss.mean(excludeBatchAxis(loss, batchAxis));
    }
}