package io.smooth.data.modify

import io.smooth.data.source.operation.modifiying.DeleteOperation
import io.smooth.data.source.operation.modifiying.SaveOperation
import io.smooth.data.source.operation.modifiying.UpdateOperation

internal val OPPOSITE_OPERATIONS = mapOf(
        SaveOperation::class to DeleteOperation::class,
        DeleteOperation::class to SaveOperation::class,
        UpdateOperation::class to UpdateOperation::class
    )