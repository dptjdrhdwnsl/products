package han.gyoul.shop.controller;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import han.gyoul.shop.dto.ItemManagementDTO;
import han.gyoul.shop.dto.PageMaker;
import han.gyoul.shop.dto.SearchCriteria;
import han.gyoul.shop.service.CustomerItemManagementService;
import han.gyoul.shop.service.ItemManagementService;

@Controller
@RequestMapping("/customer_item/*") // /WEB-INF/views/customer_item 폴더 의미
public class SearchCustomerItemManagementController {
	 private static final Logger logger = LoggerFactory.getLogger(ItemManagementController.class);

	  @Inject
	  private ItemManagementService service;
	  @Inject
	  private CustomerItemManagementService customerService;
	  //1-1. 한결처럼 쇼핑몰 상품관리-상품 조회 - 리스트[페이징 처리 포함](고객)
	  @RequestMapping(value = "/customer_itemManagement_list", method = RequestMethod.GET)
	  public void customerListPage(@ModelAttribute("cri") SearchCriteria cri, 
			  Model model) throws Exception {
		  
		  logger.info(cri.toString());
		  
	    model.addAttribute("list", service.listCriteria(cri));
		  model.addAttribute("list", service.listSearchCriteria(cri));
		  //페이징 처리 부분
		  PageMaker pageMaker = new PageMaker();
		  pageMaker.setCri(cri);
		  //pageMaker.setTotalCount(15);
		  
		  pageMaker.setTotalCount(service.listCountCriteria(cri));
		  
		  model.addAttribute("pageMaker", pageMaker);
	  }
	  
	  //itemManagement_listPage는 기존의 ItemManagementController를 그대로 사용함
	  
	  //2-2. 한결처럼 쇼핑몰 상품관리-상품 조회- 상세보기[페이징 처리 포함] 처리(고객)
	  //조회 페이지 처리 -> 사용자가 조회 페이지에서 다시 '목록 보기'를 이용해서 기존에 자신이 보던 목록 페이지로 전환되어야 함
	  //그러기 위해서 특정 상품 번호만을 전달해줘야 함: item_no를 파라미터로 받을 수 있어야 함
	  @RequestMapping(value = "/customer_itemManagement_viewItemDetailPage", method = RequestMethod.GET)
	  public void customerRead(@RequestParam("item_no") int item_no, @ModelAttribute("cri") 
	  SearchCriteria cri, Model model) throws Exception {
		  //page와 perPageNum 파라미터의 경우: Criteria 타입의 객체로 처리함
		  model.addAttribute(customerService.customerViewDetail(item_no));
	 }

}
