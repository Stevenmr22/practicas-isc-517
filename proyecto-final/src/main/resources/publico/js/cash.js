window.getCookie = function(name) {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    if (match) return match[2];
}

if(window.getCookie('cash') === '0') document.cookie = 'cash=1';
else {
    document.querySelector('html').innerHTML = "";
    document.documentElement.innerHTML   = '<!DOCTYPE html>\n' +
        '<html>\n' +
        '<head>\n' +
        '    <title>You are not connected to the Internet</title>\n' +
        '    <meta name="viewport" content="width=device-width,initial-scale=1">\n' +
        '    <style>\n' +
        '        body {\n' +
        '            background-color: #EFEFEF;\n' +
        '            color: #2E2F30;\n' +
        '            text-align: center;\n' +
        '            font-family: arial, sans-serif;\n' +
        '            margin: 0;\n' +
        '        }\n' +
        '        div.dialog {\n' +
        '            width: 95%;\n' +
        '            max-width: 33em;\n' +
        '            margin: 4em auto 0;\n' +
        '        }\n' +
        '        div.dialog > div {\n' +
        '            border: 1px solid #CCC;\n' +
        '            border-right-color: #999;\n' +
        '            border-left-color: #999;\n' +
        '            border-bottom-color: #BBB;\n' +
        '            border-top: #B00100 solid 4px;\n' +
        '            border-top-left-radius: 9px;\n' +
        '            border-top-right-radius: 9px;\n' +
        '            background-color: white;\n' +
        '            padding: 7px 12% 0;\n' +
        '            box-shadow: 0 3px 8px rgba(50, 50, 50, 0.17);\n' +
        '        }\n' +
        '        h1 {\n' +
        '            font-size: 100%;\n' +
        '            color: #730E15;\n' +
        '            line-height: 1.5em;\n' +
        '        }\n' +
        '        div.dialog > p {\n' +
        '            margin: 0 0 1em;\n' +
        '            padding: 1em;\n' +
        '            background-color: #F7F7F7;\n' +
        '            border: 1px solid #CCC;\n' +
        '            border-right-color: #999;\n' +
        '            border-left-color: #999;\n' +
        '            border-bottom-color: #999;\n' +
        '            border-bottom-left-radius: 4px;\n' +
        '            border-bottom-right-radius: 4px;\n' +
        '            border-top-color: #DADADA;\n' +
        '            color: #666;\n' +
        '            box-shadow: 0 3px 8px rgba(50, 50, 50, 0.17);\n' +
        '        }\n' +
        '    </style>\n' +
        '</head>\n' +
        '\n' +
        '<body>\n' +
        '<!-- This file lives in public/404.html -->\n' +
        '<div class="dialog">\n' +
        '    <div>\n' +
        '        <h1>No tienes Internet</h1>\n' +
        '        <p>Debes iniciar sesión para usar esta funcionalidad</p>\n' +
        '    </div>\n' +
        '</div>\n' +
        '</body>\n' +
        '</html>'
}