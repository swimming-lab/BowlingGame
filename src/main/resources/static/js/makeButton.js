// 버튼 title 태그 결정 함수
function getBtnTitle(num) {
    switch (num) {
        case 0:
            return `No pins knocked down`;
        case 1:
            return `1 pin knocked down`;
        default:
            return `${num} pins knocked down`;
    }
}

// 버튼 생성 함수
function makeBtnSet(num) {
    $("#btn").text("");
    for (let i = 0; i <= num; i++) {
        $("#btn").append(`
            <button
                id="btn${i}"
                type="button"
                class="btn
                btn-default"
                data-toggle="tooltip"
                data-placement="bottom"
                title="${getBtnTitle(i)}"
                value="${i}">${i}</button>`
        );
        $(`#btn${i}`).click(getScoreValue);
    }
}
