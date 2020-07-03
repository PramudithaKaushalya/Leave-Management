# react-scrollpad
> &lt;ScrollPad/> - get scrollbar with padding (when overflown)

### installation

> `npm install --save react-scrollpad`

### Usage

```jsx
import React from 'react';
import {render} from 'react-dom';

import ScrollPad from 'react-scrollpad';

const App = () => (
  <div className="wrapper">

    <ScrollPad>

      <div>
        <p>Scroll</p>
        <p>Scroll</p>
        <p>Scroll</p>
        ...
      </div>

    </ScrollPad>

  </div>
);

render(<App/>, document.getElementById("root"));
```

### Available props

**prop**|**type**|**default**
:-----:|:-----:|:-----:
style | PropTypes.object | {}
rtl | PropTypes.bool | false
pad | PropTypes.string | 20px
className | PropTypes.string |

### Demo

> https://sanusart.github.io/react-scrollpad/
