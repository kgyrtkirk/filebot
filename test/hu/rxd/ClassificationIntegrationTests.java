package hu.rxd;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import hu.rxd.filebot.ScanBot;
import hu.rxd.filebot.tree.MediaSection;
import hu.rxd.filebot.tree.MediaSection.ISection;
import hu.rxd.filebot.tree.MediaSection.Root;
import hu.rxd.filebot.tree.MediaTag;

//@RunWith(Parameterized.class)
public class ClassificationIntegrationTests {

	// @Parameters(name="{0}")
	// public static Iterable<Object[]> getParameters(){
	//
	// List<Object[]> list=new ArrayList<>();
	// list.add(new Object[]{basicSections("The Good Wife S05","20 - The Deep
	// Web.mkv")});
	// return list;
	//
	// }
	
	
	public static ISection getLeaf(Root root) {
		ISection curr = root;
		while (curr.getChildren().size() > 0) {
			curr = curr.getChildren().iterator().next();
		}
		return curr;
	}
	
	public static MediaSection.Root basicSections(String... strings) {
		Root r = new MediaSection.Root("/");
		ISection s = r;
		for (int i = 0; i < strings.length - 1; i++) {
			s = s.getSubsection(strings[i]);
		}
		s.getEntry(strings[strings.length - 1]);
		return r;
	}

	// public ClassificationIntegrationTests(String a,String b){
	// System.out.println(a);
	// }

	@Test
	public void goodWife() throws Exception {
		Root root = basicSections("The Good Wife S05", "20 - The Deep Web.mkv");
		ScanBot.runIdentification(root);
		ISection leaf = getLeaf(root);
		assertEquals(20,leaf.getTag(MediaTag.episode));
		assertEquals(5,leaf.getTag(MediaTag.season));
		assertEquals("The Good Wife",leaf.getTag(MediaTag.series));
//		assertEquals("20",leaf.getTagByName("series").getValue());

	}
	@Test
	public void empire() throws Exception {
		
		Root root = basicSections("Empire S01 1080p", "1x10 - Sins Of The Father 1080p Bluray.mkv");
		ScanBot.runIdentification(root);
		ISection leaf = getLeaf(root);
		assertEquals(10,leaf.getTag(MediaTag.episode));
		assertEquals(1,leaf.getTag(MediaTag.season));
		assertEquals("Empire (2015)",leaf.getTag(MediaTag.series));
		System.out.println(leaf);
	}
	@Test
	public void badu() throws Exception {
		
		Root root = basicSections("Bad.Education.S01.COMPLETE.HUN.WEB-DL.XviD-DART","dt-badu-102.avi");
		ScanBot.runIdentification(root);
		ISection leaf = getLeaf(root);
		assertEquals(2,leaf.getTag(MediaTag.episode));
		assertEquals(1,leaf.getTag(MediaTag.season));
		assertEquals("Bad Education",leaf.getTag(MediaTag.series));
	}
	@Test
	public void szomszedok() throws Exception {
		
		Root root = basicSections("	Szomszedok.Complete.DVDRiP.XviD-x264.Hun-MrX","Szomszedok.1987.Se01.Complete.DVBRip.XviD.Hun-MrX","Szomszedok.1987.Se01.Ep03.DVBRip.XviD.Hun-MrX.avi");
		ScanBot.runIdentification(root);
		ISection leaf = getLeaf(root);
		assertEquals(3,leaf.getTag(MediaTag.episode));
		assertEquals(1,leaf.getTag(MediaTag.season));
		assertEquals("Szomszedok",leaf.getTag(MediaTag.series));
	}
	
	
	@Test
	public void psych() throws Exception {
		Root root = basicSections("Psych.S03.HUN.DVDRip.XviD-HSF","Psych.S03E15.REPACK.HUN.DVDRip.XviD-HSF","hsf-psych-rpck-315.avi");
		ScanBot.runIdentification(root);
		ISection leaf = getLeaf(root);
		assertEquals(15,leaf.getTag(MediaTag.episode));
		assertEquals(3,leaf.getTag(MediaTag.season));
		assertEquals("Psych",leaf.getTag(MediaTag.series));
		assertEquals("Psych/S03E15.Tuesday the 17th.avi", leaf.getTag(MediaTag.seriesOutput));
//		assertEquals(expected, actual);
	}
	
	@Test
	public void dom() throws Exception {
		Root root = basicSections("d1","movies","Daddy.or.Mommy.2015.PROPER.RETAiL.DVDRip.x264.HuN-No1","dom.dvdrip.x264-no1.mkv");
		ScanBot.runIdentification(root);
		ISection leaf = getLeaf(root);
		assertEquals("Daddy or Mommy",leaf.getTag(MediaTag.movie));
//		assertEquals("3",leaf.getTag(MediaTagKey.season).getValue());
//		assertEquals("Psych",leaf.getTag(MediaTagKey.series).getValue());
//		assertEquals("Psych/S03E15.Tuesday the 17th.avi", leaf.getTag(MediaTagKey.seriesOutput).getValue());
//		assertEquals(expected, actual);
	}
	
	@Test
	public void exp3() throws Exception {

		Root root = basicSections("The.Expendables.3.2014.THEATRiCAL.720p.BluRay.DTS.x264.HuN-TRiNiTY","the.expendables.3.tc.720p-trinity.mkv");
		ScanBot.runIdentification(root);
		ISection leaf = getLeaf(root);
		assertEquals("The Expendables 3",leaf.getTag(MediaTag.movie));
	}

	@Test
	public void hfo() throws Exception {

		Root root = basicSections("Hawaii.Five-0.2010.The.Complete.S01.HDTV.XviD-HUN-SKH","Hawaii.Five-0.2010.S01E20.HDTV.XviD.HUN-SKH.avi");
		ScanBot.runIdentification(root);
		ISection leaf = getLeaf(root);
		assertEquals("Hawaii Five-0",leaf.getTag(MediaTag.series));
	}
	@Test
	public void spa() throws Exception {

		Root root = basicSections("Spartacus.COMPLETE.BDRIP.x264.Hun.Eng-Krissz","Spartacus.Blood.and.Sand.S01.BDRIP.x264.Hun.Eng-Krissz","Spartacus.Blood.and.Sand.S01E04.BDRIP.x264.Hun.Eng-Krissz.mp4");
		ScanBot.runIdentification(root);
		ISection leaf = getLeaf(root);
		System.out.println(leaf);
		assertEquals("Spartacus",leaf.getTag(MediaTag.series));
	}
	
	@Test
	public void hfo2() throws Exception {
	Root root = basicSections("Hawaii.Five-0.S02.HDTV.XviD.Hun-SLN","Hawaii.Five-0.S02E14.HDTV.XviD.Hun-SLN","Hawaii.Five-0.S02E14.HDTV.XviD.Hun-SLN.avi");
	ScanBot.runIdentification(root);
	ISection leaf = getLeaf(root);
	assertEquals("Hawaii Five-0",leaf.getTag(MediaTag.series));
	}
	
	@Test
	public void spy() throws Exception {
	Root root = basicSections("Spy.2015.Extended.Cut.BDRip.x264.HuN-HRT","hrt-spy.extended.cut.2015.bdrip.x264.mkv");
	ScanBot.runIdentification(root);
	ISection leaf = getLeaf(root);
	assertEquals("Spy",leaf.getTag(MediaTag.movie));
	}
	
	@Test
	public void a1976() throws Exception {
	Root root = basicSections("Assault.on.Precinct.13.1976.DVDRip.XviD.Hun-JM","Assault.on.Precinct.13.1976.DVDRip.XviD.Hun-JM.avi");
	ScanBot.runIdentification(root);
	ISection leaf = getLeaf(root);
	assertEquals("Assault on Precinct 13",leaf.getTag(MediaTag.movie));
	System.out.println(leaf);
	assertEquals("Assault on Precinct 13 (1976)/Assault on Precinct 13 (1976).avi",leaf.getTag(MediaTag.movieOutput));
	}
	@Test
	public void ff() throws Exception {
	Root root = basicSections("Fantastic.Four.2015.RETAiL.BDRip.x264.Hungarian-nCORE","ncore-FF.mkv");
	ScanBot.runIdentification(root);
	ISection leaf = getLeaf(root);
	System.out.println(leaf);
	assertEquals("Fantastic Four (2015)/Fantastic Four (2015).mkv",leaf.getTag(MediaTag.movieOutput));
	}
	
	
}
