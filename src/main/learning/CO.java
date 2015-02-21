//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.junit.Test;
//
//import util.Node;
//
//	/*-
//	 *         root
//	 *       /     \   
//	 *      l       r
//	 *            /  \    
//	 *    -2=>  rl    rr
//	 *         /  \
//	 *       rll  rlr
//	 */
//	@Test
//	public void mutationIsPerformedOnProperNodeMiddleTree() {
//		Node<Integer> rl = initializeChromozomeAndGetRealNode(true);
//		when(r.nextDouble()).thenReturn(BIGGER).thenReturn(BIGGER)
//				.thenReturn(BIGGER).thenReturn(SMALLER).thenReturn(SMALLER);
//		when(r.nextInt(2)).thenReturn(1);
//
//		ChromosomeOperator.mutation(chromosome, r);
//
//		assertEquals(-2, (int) rl.getData());
//	}
//
//	/*-
//	 *         root
//	 *       /     \   
//	 *      l       r   <=-2
//	 *            /  \    
//	 *          rl    rr
//	 */
//	@Test
//	public void mutationIsPerformedOnProperNodeWhenTerminalReached() {
//		Node<Integer> rl = initializeChromozomeAndGetRealNode(false);
//		when(r.nextDouble()).thenReturn(BIGGER).thenReturn(BIGGER)
//				.thenReturn(BIGGER).thenReturn(SMALLER).thenReturn(BIGGER);
//		when(r.nextInt(2)).thenReturn(1);
//
//		ChromosomeOperator.mutation(chromosome, r);
//
//		assertEquals(1, (int) rl.getData());
//		verify(rl.getParent()).setData(-2);
// // }