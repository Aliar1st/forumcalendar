$(function () {

    $(document).ready(function () {

        var hrefStack = getStack();

        // if (location.href.search(/http:\/\/localhost:8080\/menu/i) !== -1) {
        //     setStack(['http://localhost:8080/menu']);
        // } else if (hrefStack[hrefStack.length - 1] !== location.href) {
        //     hrefStack.push(location.href);
        //     setStack(hrefStack);
        // }

        if (location.href.search(/http:\/\/forum-calendar.ddns.net:8080\/menu/i) !== -1) {
            setStack(['http://forum-calendar.ddns.net:8080/menu']);
        } else if (hrefStack[hrefStack.length - 1] !== location.href) {
            hrefStack.push(location.href);
            setStack(hrefStack);
        }
    });

    $.goBack = function () {
        var hrefStack = getStack();
        hrefStack.pop();

        var back = hrefStack.pop();
        if (back === undefined) {
            history.back();
        }
        else if (back.search(/edit/i) !== -1) {
            hrefStack.pop();
            window.location.href = hrefStack.pop();
        }
        else {
            window.location.href = back;
        }
        setStack(hrefStack);
    };

    function setStack(hrefStack) {
        $.cookie('hrefStack', JSON.stringify(hrefStack), {path: '/'});
    }

    function getStack() {
        var hrefStackString = $.cookie('hrefStack'); // получаем сохраненные ранее настройки

        if (hrefStackString === undefined) {
            return [];
        }
        else {
            return eval(hrefStackString);
        }
    }

});





