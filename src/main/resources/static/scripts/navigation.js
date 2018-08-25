$(function () {

    $(document).ready(function () {

        var hrefStack = getStack();

        if (hrefStack[hrefStack.length - 1] !== location.href) {
            hrefStack.push(location.href);
            setStack(hrefStack);
        }

    });

    $.goBack = function () {
        var hrefStack = getStack();
        hrefStack.pop();

        var back = hrefStack.pop();
        if (back.search(/edit/i) !== -1) {
            hrefStack.pop();
            window.location.href = hrefStack.pop();
        }
        else if (back === undefined) {
            history.back();
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





