package han.gyoul.shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import han.gyoul.shop.category.dto.CategoryDTO;
import han.gyoul.shop.category.service.CategoryService;
import han.gyoul.shop.dto.ItemManagementDTO;
import han.gyoul.shop.dto.PageMaker;
import han.gyoul.shop.dto.SearchCriteria;
import han.gyoul.shop.service.ItemManagementService;




@Controller
@RequestMapping("/item/*") // /WEB-INF/views/item 폴더 의미
public class SearchItemManagementController {
	
	 private static final Logger logger = LoggerFactory.
			 getLogger(SearchItemManagementController.class);

	  @Inject //의존성 주입 의미
	  private ItemManagementService service;
	  @Inject //의존성 주입 의미
	  private CategoryService categoryService;
	  
	  //1. 한결처럼 쇼핑몰 상품관리-상품 조회 - 리스트[페이징 처리 포함]
	  @RequestMapping(value = "/itemManagement_list", method = RequestMethod.GET)
	  public void listPage(@ModelAttribute("cri") SearchCriteria cri, 
			  Model model) throws Exception {
	
	    logger.info(cri.toString());
	
//	    model.addAttribute("list", service.listCriteria(cri));
	    model.addAttribute("list", service.listSearchCriteria(cri));
	    //페이징 처리 부분
	    PageMaker pageMaker = new PageMaker();
	    pageMaker.setCri(cri);
	    //pageMaker.setTotalCount(15);
	    
  //    pageMaker.setTotalCount(service.listCountCriteria(cri));
	    pageMaker.setTotalCount(service.listSearchCount(cri));
	
	    model.addAttribute("pageMaker", pageMaker);
	  }
	  
	  //itemManagement_listPage는 기존의 ItemManagementController를 그대로 사용함
	  
	  //2. 한결처럼 쇼핑몰 상품관리-상품 조회- 상세보기[페이징 처리 포함] 처리
	  //조회 페이지 처리 -> 사용자가 조회 페이지에서 다시 '목록 보기'를 이용해서 기존에 자신이 보던 목록 페이지로 전환되어야 함
	  //그러기 위해서 특정 상품 번호만을 전달해줘야 함: item_no를 파라미터로 받을 수 있어야 함
	  @RequestMapping(value = "/itemManagement_viewItemDetailPage", method = RequestMethod.GET)
	  public void read(@RequestParam("item_no") int item_no, @ModelAttribute("cri") 
	  SearchCriteria cri, Model model) throws Exception {
	    //page와 perPageNum 파라미터의 경우: Criteria 타입의 객체로 처리함
	    model.addAttribute(service.viewDetail(item_no));
	  }
	  
	  //1-1. 한결처럼 쇼핑몰 상품관리-상품 조회 - 리스트[페이징 처리 포함](고객)
//	  @RequestMapping(value = "/customer_itemManagement_list", method = RequestMethod.GET)
//	  public void customerListPage(@ModelAttribute("cri") SearchCriteria cri, 
//			  Model model) throws Exception {
//		  
//		  logger.info(cri.toString());
		  
//	    model.addAttribute("list", service.listCriteria(cri));
//		  model.addAttribute("list", service.listSearchCriteria(cri));
		  //페이징 처리 부분
//		  PageMaker pageMaker = new PageMaker();
//		  pageMaker.setCri(cri);
		  //pageMaker.setTotalCount(15);
		  
	//	  pageMaker.setTotalCount(service.listCountCriteria(cri));
		  
	//	  model.addAttribute("pageMaker", pageMaker);
	//  }
	  
	  //itemManagement_listPage는 기존의 ItemManagementController를 그대로 사용함
	  
	  //2-2. 한결처럼 쇼핑몰 상품관리-상품 조회- 상세보기[페이징 처리 포함] 처리(고객)
	  //조회 페이지 처리 -> 사용자가 조회 페이지에서 다시 '목록 보기'를 이용해서 기존에 자신이 보던 목록 페이지로 전환되어야 함
	  //그러기 위해서 특정 상품 번호만을 전달해줘야 함: item_no를 파라미터로 받을 수 있어야 함
	  //@RequestMapping(value = "/customer_itemManagement_viewItemDetailPage", method = RequestMethod.GET)
	  //public void customerRead(@RequestParam("item_no") int item_no, @ModelAttribute("cri") 
	  //SearchCriteria cri, Model model) throws Exception {
		  //page와 perPageNum 파라미터의 경우: Criteria 타입의 객체로 처리함
		 // model.addAttribute(service.customerViewDetail(item_no));
	//  }
	  
	  //3. 한결처럼 쇼핑몰 상품관리-상품 삭제[페이징 처리 포함] 처리
	//삭제 페이지 처리: 조회 페이지에서 삭제 -> SearchItemManagementController -> 목록 페이지
	  @RequestMapping(value = "/itemManagement_deletePage", method = RequestMethod.POST)
	  public String remove(@RequestParam("item_no") int item_no, SearchCriteria cri, 
			  RedirectAttributes rttr) throws Exception {
	
	    service.delete(item_no);
	
	    rttr.addAttribute("page", cri.getPage());
	    rttr.addAttribute("perPageNum", cri.getPerPageNum());
	    rttr.addAttribute("searchType", cri.getSearchType());
	    rttr.addAttribute("keyword", cri.getKeyword());
	    
	    rttr.addFlashAttribute("msg", "SUCCESS");
	    //삭제 결과는 임시로 사용하는 데이터이므로 addFlashAttribute()를 이용해서 처리함
	
	    return "redirect:/item/itemManagement_list";
	  }
	  
	  //4. 한결처럼 쇼핑몰 상품관리-상품 수정[페이징 처리 포함] 처리
	  //4-1. 수정 페이지 처리: 조회 페이지-> SearchItemManagementController -> 수정 페이지 -> SearchItemManagementController -> 조회 페이지
	  @RequestMapping(value = "/itemManagement_modifyPage", method = RequestMethod.GET)
	  //수정 작업에 유지해야 하는 정보가 포함되어야 하므로 itemManagement_modifyPage.jsp 작성
	  public void modifyPagingGET(@RequestParam("item_no") int item_no, @ModelAttribute("cri") 
	  SearchCriteria cri, Model model)
	      throws Exception {
	
	    model.addAttribute(service.viewDetail(item_no));
	  }
	  //4-2. 수정 페이지 처리: 조회 페이지-> SearchItemManagementController -> 수정 페이지 -> SearchItemManagementController -> 조회 페이지
	  @RequestMapping(value = "/itemManagement_modifyPage", method = RequestMethod.POST)
	  //수정 작업에 유지해야 하는 정보가 포함되어야 하므로 itemManagement_modifyPage.jsp 작성
	  public String modifyPagingPOST(ItemManagementDTO itemManagementDTO, 
	  SearchCriteria cri, RedirectAttributes rttr) throws Exception {
		  
		  logger.info(cri.toString());
		  service.modify(itemManagementDTO);
		  
		  rttr.addAttribute("page", cri.getPage());
		  rttr.addAttribute("perPageNum", cri.getPerPageNum());
		  rttr.addAttribute("searchType", cri.getSearchType());
		  rttr.addAttribute("keyword", cri.getKeyword());
		    
		  rttr.addFlashAttribute("msg", "SUCCESS");
		  //수정 작업이 끝나면 삭제와 마찬가지로 다시 목록 페이지로 리다이렉트 되어야 하고, 수정된 결고에 대해서는 'msg'로 전송
		  
		  logger.info(rttr.toString());
		  
		return "redirect:/item/itemManagement_list";
		//수정 작업: 특정 페이지 내에서 특정 게시물 조회하고, 다시 수정 택함
		//수정이 가능한 화면에서는 POST 방식으로 수정을 요청하게 되고 작업 완료 후
		//원래의 정보가 유지된 채로 목록 페이지로 이동
	  }

	  //5. 한결처럼 쇼핑몰 상품관리-상품 등록[페이징 처리 포함] 처리
	  
	  //5-1. 상품 등록 폼
	  @RequestMapping(value = "/itemManagement_register", method = RequestMethod.GET)
	   ///WEB-INF/views/itemManagement/itemManagement_register.jsp 의미
	  public void registerGET(ItemManagementDTO itemManagementDTO, Model model) throws Exception {
		  categoryService.getCategoryList();
		  
		  model.addAttribute("list", categoryService.getCategoryList());
		  
		  logger.info("register get ...........");
	  }
	  
//	  //5-1.1 소분류목록 호출
//	  @RequestMapping(value = "/subclassList", method = RequestMethod.GET)
//	  public void subclassList(CategoryDTO categoryDTO, Model model) throws Exception {
//		  
//		  categoryService.getSubclassList();
//		  
//		  model.addAttribute("sublist", categoryService.getSubclassList());
//		  
//		  logger.info("register get ...........");
//	  }
	  
		 
		  
	  //5-2. 상품 등록 처리 폼
	  //RedirectAttributes를 이용한 숨김 데이터를 전송하기 위해 addFlashAttribute()이용
	   @RequestMapping(value = "/itemManagement_register", method = RequestMethod.POST)
	   public String registPOST(@ModelAttribute ItemManagementDTO itemManagementDTO, RedirectAttributes rttr) throws Exception {
		   
		   logger.info("regist post ...........");
		   logger.info(itemManagementDTO.toString());
		   
		   service.regist(itemManagementDTO);
		  
		    rttr.addFlashAttribute("msg", "SUCCESS");
		   //브라우저에서 msg로 추가된 데이터는 보여지지 않음
		   
		   //return "/itemManagement/success";
		  // return "redirect:/itemManagement/itemManagement_listAll";
		   return "redirect:/item/itemManagement_list";
	   }
}
