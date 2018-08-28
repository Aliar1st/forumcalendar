$(function () {

    var domain = 'localhost';
    var port = '8090';

    var menuHref = 'http://' + domain + ':' + port + '/menu';

    $(window).on('popstate', function () {
        $.goBack();
    });

    $(document).ready(function () {
        history.pushState(null, null, location.href);

        var hrefStack = getStack();

        if (location.href.search(/http:\/\/localhost:8090\/menu/i) !== -1) {
            setStack([menuHref]);
        }
        else {
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
        else if (back.search(/edit/i) !== -1 || back.search(/add/i) !== -1) {
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
            //return ['http://forum-calendar.ddns.net:8080/menu'];
            return [menuHref];
        }
        else {
            return eval(hrefStackString);
        }
    }

});

// $(function () {
//
//     var domain = 'forum-calendar.ddns.net';
//     var port = '8080';
//
//     var menuHref = 'http://' + domain + ':' + port + '/menu';
//
//     $(window).on('popstate', function () {
//         $.goBack();
//     });
//
//
//     $(document).ready(function () {
//         history.pushState(null, null, location.href);
//
//         var hrefStack = getStack();
//
//         if (location.href.search(/http:\/\/forum-calendar.ddns.net:8080\/menu/i) !== -1) {
//             setStack([menuHref]);
//         }
//         else {
//             hrefStack.push(location.href);
//             setStack(hrefStack);
//         }
//     });
//
//     $.goBack = function () {
//         var hrefStack = getStack();
//         hrefStack.pop();
//
//         var back = hrefStack.pop();
//         if (back === undefined) {
//             history.back();
//         }
//         else if (back.search(/edit/i) !== -1 || back.search(/add/i) !== -1) {
//             hrefStack.pop();
//             window.location.href = hrefStack.pop();
//         }
//         else {
//             window.location.href = back;
//         }
//         setStack(hrefStack);
//     };
//
//     function setStack(hrefStack) {
//         $.cookie('hrefStack', JSON.stringify(hrefStack), {path: '/'});
//     }
//
//     function getStack() {
//         var hrefStackString = $.cookie('hrefStack'); // получаем сохраненные ранее настройки
//
//         if (hrefStackString === undefined) {
//             return [menuHref];
//         }
//         else {
//             return eval(hrefStackString);
//         }
//     }
//
// });











