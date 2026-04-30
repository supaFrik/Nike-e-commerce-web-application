(function () {
  function tabs(menuName, contentName) {
    $(menuName).find("li").each(function () {
      $(this).on("click", function () {
        $(menuName).find("li").removeClass("active");
        $(contentName).children().removeClass("active");
        const clicked = $(this).find("a").attr("href");
        $(this).addClass("active");
        $(contentName).find(clicked).addClass("active");
      });
    });
  }

  $(document).ready(function () {
    tabs(".tab", ".contents");
  });
})();
