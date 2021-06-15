<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="proj21_funding.service.impl.CategoryServiceImpl"%>
<%@ page import="proj21_funding.service.CategoryService"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>등록하기 | 공지사항</title>
<link rel="stylesheet" href="/proj21_funding/css/servicecenter_write.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/home_css/main.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript">	
</script>
</head>
<body>
${board }<br>
	<section class="container">
		<header>
			<jsp:include page="/WEB-INF/view/home/header_top.jsp" />
			<jsp:include page="/WEB-INF/view/home/header_account.jsp" />
		</header>
		<section class="sevicecenter_write">
		<h2>글 수정</h2>
		<form action="<%=request.getContextPath() %>/noticeupsuccess" method="post">
				<table>
					<tr>
						<td><input type="hidden" name="boardNo" id="boardNo" value="${board.boardNo }" /></td>
					</tr>
					<tr>
						<td class="td_left"><label for="categoryNo">분류</label></td>
						<td class="td_right">
							<select id="categoryNo.categoryNo" name="categoryNo.categoryNo">
								<c:forEach var="bc" items="${bc }" varStatus="status">
									<c:if test="${bc.categoryClass eq 'board' }">
										<c:choose>
											<c:when test="${bc.categoryNo eq board.categoryNo.categoryNo}"><option class="bc" value="${bc.categoryNo }" selected>${bc.categoryName }</option></c:when>
											<c:otherwise><option class="bc" value="${bc.categoryNo }">${bc.categoryName }</option></c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td class="td_left"><label for="boardTitle">제목</label></td>
						<td class="td_right"><input type="text" name="boardTitle"
							id="boardTitle" required="required" value="${board.boardTitle }"></td>
					</tr>
					<tr>
						<td class="td_left"><label for="boardContent">내용</label></td>
						<td class="td_right"><textarea name="boardContent"
								id="boardContent" cols="40" rows="15" required="required">${board.boardContent }</textarea></td>
					</tr>
				</table>
				<section id="commandCell">
					<input type="submit" value="등록">&nbsp;&nbsp;
					<a href="<%=request.getContextPath()%>/board/notice_all"><button id="go_notice">취소</button></a>
				</section>
			</form>
		</section>
		<footer>
			<jsp:include page="/WEB-INF/view/home/footer_menu.jsp" />
			<jsp:include page="/WEB-INF/view/home/footer_info.jsp" />
		</footer>


	</section>
</body>
</html>