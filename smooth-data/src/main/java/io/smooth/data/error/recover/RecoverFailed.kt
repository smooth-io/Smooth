package io.smooth.data.error.recover

import io.smooth.data.error.DataError

class RecoverFailed(): DataError("Can't recover data in source, as it doesn't exists in the source before")