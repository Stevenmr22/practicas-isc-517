
async function loadImage(url, element) {
    const data = { q: url };
    const protocol = location.protocol === 'https:' ? 'https://' : 'http://';
    fetch(protocol+location.hostname+':'+location.port+'/api-previewer', { //CHANGE HTTP
        method: "POST",
        mode: "cors",
        body: JSON.stringify(data)
    })
        .then((res) => res.json())
        .then((response) => element.src = response.image)
        .catch((error) => console.error('Error:', error));
}

function checkUrl (string) {
    let givenURL;
    try {
        givenURL = new URL (string);
    } catch (error) {
        return false;
    }
    return true;
}