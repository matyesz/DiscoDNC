/*
 * This file is part of the Disco Deterministic Network Calculator v2.3.3 "Centaur".
 *
 * Copyright (C) 2013 - 2017 Steffen Bondorf
 *
 * Distributed Computer Systems (DISCO) Lab
 * University of Kaiserslautern, Germany
 *
 * http://disco.cs.uni-kl.de
 *
 *
 * The Disco Deterministic Network Calculator (DiscoDNC) is free software;
 * you can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software Foundation; 
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

package unikl.disco.tests;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import unikl.disco.nc.AnalysisConfig.Multiplexing;
import unikl.disco.nc.AnalysisResults;
import unikl.disco.nc.Analysis.Analyses;
import unikl.disco.nc.analyses.PmooAnalysis;
import unikl.disco.nc.analyses.SeparateFlowAnalysis;
import unikl.disco.nc.analyses.TotalFlowAnalysis;
import unikl.disco.network.Flow;
import unikl.disco.network.Network;
import unikl.disco.numbers.NumFactory;

@RunWith(value = Parameterized.class)
/**
 * 
 * @author Steffen Bondorf
 *
 */
public class FF_4S_1SC_3F_1AC_3P_Test extends FunctionalTests {
	private static FF_4S_1SC_3F_1AC_3P_Network test_network;
	private static Network network;
	private static Flow f0, f1, f2;

	protected static TestResults expected_results = new TestResults();
	
	public FF_4S_1SC_3F_1AC_3P_Test( FunctionalTestConfig test_config ) {
		super( test_config );
	}
	
	@BeforeClass
	public static void createNetwork() {
		test_network = new FF_4S_1SC_3F_1AC_3P_Network();
		f0 = test_network.f0;
		f1 = test_network.f1;

		network = test_network.getNetwork();
		
		initializeBounds();
	}

	private static void initializeBounds() {
		expected_results.clear();
		
		// TFA
		expected_results.setBounds( Analyses.TFA, Multiplexing.FIFO, f0, new AnalysisResults( NumFactory.create( 5985, 64 ), NumFactory.create( 9425, 16 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.FIFO, f1, new AnalysisResults( NumFactory.create( 3965, 64 ), NumFactory.create( 9425, 16 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.FIFO, f2, new AnalysisResults( NumFactory.create( 885, 16 ), NumFactory.create( 1825, 4 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.ARBITRARY, f0, new AnalysisResults( NumFactory.create( 6425, 36 ), NumFactory.create( 6125, 9 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.ARBITRARY, f1, new AnalysisResults( NumFactory.create( 11975, 90 ), NumFactory.create( 6125, 9 ), null ) );
		expected_results.setBounds( Analyses.TFA, Multiplexing.ARBITRARY, f2, new AnalysisResults( NumFactory.create( 685, 6 ), NumFactory.create( 1475, 3 ), null ) );
		
		// SFA
		expected_results.setBounds( Analyses.SFA, Multiplexing.FIFO, f0, new AnalysisResults( NumFactory.create( 1795, 24 ), NumFactory.create( 3125, 8 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.FIFO, f1, new AnalysisResults( NumFactory.create( 10715, 192 ), NumFactory.create( 18925, 64 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.FIFO, f2, new AnalysisResults( NumFactory.create( 295, 6 ), NumFactory.create( 525, 2 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.ARBITRARY, f0, new AnalysisResults( NumFactory.create( 875, 9 ), NumFactory.create( 4525, 9 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.ARBITRARY, f1, new AnalysisResults( NumFactory.create( 2095, 27 ), NumFactory.create( 10925, 27 ), null ) );
		expected_results.setBounds( Analyses.SFA, Multiplexing.ARBITRARY, f2, new AnalysisResults( NumFactory.create( 65 ), NumFactory.create( 1025, 3 ), null ) );
		
		// PMOO
		expected_results.setBounds( Analyses.PMOO, Multiplexing.ARBITRARY, f0, new AnalysisResults( NumFactory.create( 875, 9 ), NumFactory.create( 4525, 9 ), null ) );
		expected_results.setBounds( Analyses.PMOO, Multiplexing.ARBITRARY, f1, new AnalysisResults( NumFactory.create( 2095, 27 ), NumFactory.create( 10925, 27 ), null ) );
		expected_results.setBounds( Analyses.PMOO, Multiplexing.ARBITRARY, f2, new AnalysisResults( NumFactory.create( 65 ), NumFactory.create( 1025, 3 ), null ) );
	}
	
	@Before
    public void reinitNetwork() {
		if( !super.reinitilize_numbers ){
			return;
		}
		
		test_network.reinitializeCurves();
		initializeBounds();
	}
	
//--------------------Flow 0--------------------
	@Test
	public void f0_tfa() {
		setMux( network.getServers() );
		super.runTFAtest( new TotalFlowAnalysis( network, test_config ), f0, expected_results );
	}
	
	@Test
	public void f0_sfa() {
		setMux( network.getServers() );
		super.runSFAtest( new SeparateFlowAnalysis( network, test_config ), f0, expected_results );
	}
	
	@Test
	public void f0_pmoo_arbMux() {
		setArbitraryMux( network.getServers() );
		super.runPMOOtest( new PmooAnalysis( network, test_config ), f0, expected_results );
	}
	
	@Test
	public void f0_sinktree_arbMux()
	{
		if( test_config.fullConsoleOutput() ) {
			System.out.println( "Analysis:\t\tTree Backlog Bound Analysis" );
			System.out.println( "Multiplexing:\t\tArbitrary" );
	
			System.out.println( "Flow of interest:\t" + f0.toString() );
			System.out.println();
			
			System.out.println( "--- Results: ---" );
			System.out.println( "Tree Backlog Bound calculation not applicable." );
		}
	}
	
//--------------------Flow 1--------------------
	@Test
	public void f1_tfa() {
		setMux( network.getServers() );
		super.runTFAtest( new TotalFlowAnalysis( network, test_config ), f1, expected_results );
	}

	@Test
	public void f1_sfa() {
		setMux( network.getServers() );
		super.runSFAtest( new SeparateFlowAnalysis( network, test_config ), f1, expected_results );
	}
	
	@Test
	public void f1_pmoo_arbMux() {
		setArbitraryMux( network.getServers() );
		super.runPMOOtest( new PmooAnalysis( network, test_config ), f1, expected_results );
	}
	
	@Test
	public void f1_sinktree_arbMux()
	{
		if( test_config.fullConsoleOutput() ) {
			System.out.println( "Analysis:\t\tTree Backlog Bound Analysis" );
			System.out.println( "Multiplexing:\t\tArbitrary" );
	
			System.out.println( "Flow of interest:\t" + f1.toString() );
			System.out.println();
			
			System.out.println( "--- Results: ---" );
			System.out.println( "Tree Backlog Bound calculation not applicable." );
		}
	}
	
//--------------------Flow 2--------------------
	@Test
	public void f2_tfa() {
		setMux( network.getServers() );
		super.runTFAtest( new TotalFlowAnalysis( network, test_config ), f0, expected_results );
	}
	
	@Test
	public void f2_sfa() {
		setMux( network.getServers() );
		super.runSFAtest( new SeparateFlowAnalysis( network, test_config ), f0, expected_results );
	}
	
	@Test
	public void f2_pmoo_arbMux() {
		setArbitraryMux( network.getServers() );
		super.runPMOOtest( new PmooAnalysis( network, test_config ), f0, expected_results );
	}
	
	@Test
	public void f2_sinktree_arbMux()
	{
		if( test_config.fullConsoleOutput() ) {
			System.out.println( "Analysis:\t\tTree Backlog Bound Analysis" );
			System.out.println( "Multiplexing:\t\tArbitrary" );
	
			System.out.println( "Flow of interest:\t" + f2.toString() );
			System.out.println();
			
			System.out.println( "--- Results: ---" );
			System.out.println( "Tree Backlog Bound calculation not applicable." );
		}
	}
}