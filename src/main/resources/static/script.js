
let container = document.getElementById("container");
container.innerHTML = `
<br>navigator.userAgent = ${navigator.userAgent}
<br>navigator.language = ${navigator.language}
<br>screen.width = ${screen.width}
<br>screen.availWidth = ${screen.availWidth}
<br>screen.height = ${screen.height}
<br>screen.availHeight = ${screen.awailHeight}
<br>navigator.platform = ${navigator.platform}
<br>navigator.plugins.length = ${navigator.plugins.length}
<br>navigator.cookieEnabled = ${navigator.cookieEnabled}
<br>window.history.length = ${window.history.length}
<br>screen.colorDepth = ${screen.colorDepth}
<br>screen.pixelDepth = ${screen.pixelDepth}
<br>Intl.DateTimeFormat().resolvedOptions().timeZone = ${Intl.DateTimeFormat().resolvedOptions().timeZone}
<br>navigator.doNotTrack = ${navigator.doNotTrack}
<br>window.devicePixelRatio = ${window.devicePixelRatio}
<br>Navigator.hardwareConcurrency = ${Navigator.hardwareConcurrency}
<br>window.localStorage = ${window.localStorage}
<br>window.sessionStorage = ${window.sessionStorage}
<br>navigator.webdriver = ${navigator.webdriver}
<br>window.indexedDB = ${window.indexedDB}
<br>navigator.permissions.query = ${navigator.permissions.query}
<br>new AudioContext().destination.channelCount = ${new AudioContext().destination.channelCount}
`;
