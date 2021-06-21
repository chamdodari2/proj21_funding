package proj21_funding.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import proj21_funding.dto.PrjCategory;
import proj21_funding.dto.PrjOption;
import proj21_funding.dto.Project;
import proj21_funding.dto.account.UserInfo;
import proj21_funding.dto.project.AddPrjOption;
import proj21_funding.dto.project.UpdateProject;
import proj21_funding.exception.ProjectNotDeleteException;
import proj21_funding.exception.ProjectNotFoundException;
import proj21_funding.service.PrjCategoryService;
import proj21_funding.service.PrjOptionService;
import proj21_funding.service.ProjectAndPrjOptionService;
import proj21_funding.service.ProjectService;
import proj21_funding.service.UserInfoService;

@Controller
public class UploadController {	
	
	//web.xml에 있는 multipart-config 주소랑 동일시하게 
		private static final String UPLOAD_PATH = "C:\\workspace_web\\proj21_funding\\src\\main\\webapp\\images\\project";

	@Autowired
	private ProjectAndPrjOptionService trservice;
	
	@Autowired
	private PrjOptionService optionService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private PrjCategoryService prjCategoryService;
	
	@Autowired
	private UserInfoService userService;
	
	
//	업로드 할 시 계좌 등록 안되어있으면 계좌 등록
	@GetMapping("/registerAccount")
	public String registerAccount() {
		return "upload/register_bankaccount";
	}
	
//	home에서 프로젝트 올리기 광고페이지
	@GetMapping("/uploadMain")
	public String uploadMain() {		
		return "upload/upload_main";
	}
//	로그인 되어있을 시 파일 업로드 가능.
	@GetMapping("/uploadMain/{authInfo.userNo}")
	public ModelAndView uploadMainByNo(@PathVariable("authInfo.userNo") int userNo) {
		UserInfo list = userService.showBankAccount(userNo);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("list", list);
		mav.setViewName("upload/upload_main");	
		
		return mav;
	}
//  계좌 등록 페이지
	@PostMapping("/registerBank/{authInfo.userNo}")
	public ModelAndView updateBankAccount(@PathVariable("authInfo.userNo") int userNo, UserInfo userInfo ) {
		userService.updateBankAccount(userInfo);
		List<PrjCategory> list = prjCategoryService.showCategory();

		ModelAndView mav = new ModelAndView();		
		mav.addObject("category", list);
		mav.setViewName("upload/register");
		
		return mav;
	}
	
	//광고페이지에서 등록 html
	@GetMapping("/registerForm")
	public ModelAndView uploadRegister() {	
		List<PrjCategory> list = prjCategoryService.showCategory();
//		System.out.println("prjcategory >>" + list);
		ModelAndView mav = new ModelAndView();
		mav.addObject("category", list);
		mav.setViewName("upload/register");			
		return mav;
	}
	
	//등록 취소 후 광고페이지
	@GetMapping("/uploadListCancel")
	public  String uploadCancel() {
		return "upload/upload_main";
	}
	
	//수정 취소 후 리스트
	@RequestMapping("/updateListCancel")
	public String updateListCancel() {
		return "upload/register_success";		
	}	
	
	//업로드
	@PostMapping("/listSuccess")
	public ModelAndView registerSuccess(@Valid Project project, @Valid PrjOption prjoption, 
														@Valid AddPrjOption addPrjOption, Errors errors, MultipartFile uploadfile) {	
		ModelAndView mav = new ModelAndView();
		
		if(errors.hasErrors()) {
			mav.setViewName("upload/register");
			return mav;
		}
		
		
		List<PrjCategory> list = prjCategoryService.showCategory();		
		mav.addObject("category", list);
		mav.setViewName("upload/register");			
		try {
		//트렌젝션추가
		trservice.trJoinPrjAndPrjOpt(project, prjoption, uploadfile);	
	
		mav.setViewName("upload/register_success");			
		//옵션 추가
//		addPrjOption.setPrjNo(prjoption.getPrjNo());
//		optionService.insertAddPrjOption(addPrjOption);		
		return mav;
	
		}catch (Exception e) { 
			e.printStackTrace();
		 
		 return mav; 
		 }				
	}
	
	//등록완료페이지에서 -> 수정페이지
	@RequestMapping("/updatePrj/{prjNo}")
	public ModelAndView updateProject(@PathVariable("prjNo") int prjNo) {
//		System.out.println("prjNo >> "+ prjNo);
		List<PrjOption> project = optionService.showPrjOptionByPrjNo(prjNo);
		List<PrjCategory> list = prjCategoryService.showCategory();
//		System.out.println("project >>>> " +project);
		if(project == null) { 
			throw new ProjectNotFoundException();
		}		
		ModelAndView mav = new ModelAndView();
		mav.addObject("project", project);
		mav.addObject("category", list);
		mav.setViewName("upload/update");
		
		return mav;	
	}
	
	//수정 완료 후 리스트
	@PostMapping("/updateListSuccess")
	public ModelAndView updateListSuccess(UpdateProject project, MultipartFile uploadfile  ) throws IOException {
//		System.out.println("updateProject 전 >> " + project);
		Map<String, Object> map = new HashMap<String, Object>();	
		map.put("pNo", project.getPrjNo());
		map.put("pCategoryNo", project.getpCategoryNo().getpCategoryNo());
		map.put("pName", project.getPrjName());
		map.put("pContent", project.getPrjContent());
		map.put("pGoal", project.getPrjGoal());
		map.put("eDate", project.getEndDate());
		map.put("pDate", project.getPayDate());
		map.put("oName", project.getOptName());
		map.put("oPrice", project.getOptPrice());
		map.put("oContent", project.getOptContent());
//		System.out.println("map 후 >>> "+ map);  
		
		// 파일 업로드
//		System.out.println("project.getPrjNo() >> " + project.getPrjNo());
		String saveName = "project"+project.getPrjNo()+".jpg";
//		System.out.println("saveName>> "  +saveName);
		
		File saveFile = new File(UPLOAD_PATH, saveName);
		
		try {
			uploadfile.transferTo(saveFile);
		}catch (IOException e) {
		e.printStackTrace();
		}
		// 파일 업로드
		projectService.joinUpdateProjectAndPrjoptionByNo(map);		
		List<PrjCategory> list = prjCategoryService.showCategory();
		ModelAndView mav = new ModelAndView();
		mav.addObject("project", map);
		mav.addObject("category", list);
		mav.setViewName("upload/update_success");	
		return mav;				 
	}
//	삭제
	@RequestMapping("/removeOneProject/{prjNo}")
	public String deleteProject(@PathVariable("prjNo") int prjNo) {
		trservice.trremovePrjAndPrjOpt(prjNo);
		if(prjNo == 0) { 
			throw new ProjectNotDeleteException();
		}		
		return "/upload/register_success";
	}
		

}
