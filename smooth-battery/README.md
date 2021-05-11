## Introduction

A battery manager for Android, to simplify battery operations.

## Features

* Battery percentage
* Battery charging state
* Battery level state
* Listen for battery level state
* Listen for battery charging state
* Integrates with Smooth Constraints and Smooth Family
* Kotlin coroutines and flows support

## Download

When available will let you know.

## Usage

Battery percentage

```
SmoothBatteryManager.getInstance().getBatteryPercentage()
```

Battery charging state

```
SmoothBatteryManager.getInstance().getChargingState()
```

Battery level state

```
SmoothBatteryManager.getInstance().getLevelState()
```

Listen for battery level state

```
SmoothBatteryManager.getInstance().registerListener(object: BatteryListener{
    override fun onChargingStateChanged(batteryChargingState: BatteryChargingState) {

    }

    override fun onLevelChanged(levelState: BatteryLevelState) {
    }
})
SmoothBatteryManager.getInstance().trackBatteryEvents()
```

Listen for battery charging state

```
SmoothBatteryManager.getInstance().registerListener(object: BatteryListener{
    override fun onChargingStateChanged(batteryChargingState: BatteryChargingState) {
        
    }

    override fun onLevelChanged(levelState: BatteryLevelState) {
    }
})
SmoothBatteryManager.getInstance().trackBatteryEvents()
```

Kotlin coroutines and flows support

```
//For battery level
SmoothBatteryManager.getInstance().listenForBatteryLevel().collect { 
    
}

//For charging state
SmoothBatteryManager.getInstance().listenForBatteryChargingState().collect{
    
}
```

## Important notes

Please make sure to call clear, when you finish listening on any battery events, to avoid consuming resources, using:

```
SmoothBatteryManager.getInstance().clear()
```

## Contributing

If you've ever wanted to contribute to open source, and a great cause, now is your chance!

Pull the repo and submit a PR.

## LICENSE

```
The MIT License (MIT)
Copyright (c) 2021 Mohammed Alhammouri

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```