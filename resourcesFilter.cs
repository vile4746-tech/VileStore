using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;
using Unity.VisualScripting;

public class resourcesFilter : EditorWindow
{
    private class node
    {
        public string name;
        public bool isout;

        public node(string name, bool isout) { this.name = name;this.isout = isout; }

    }
    private static string[] guids;
    private static List<node> shows = new List<node>();
    private static List<string> paths = new List<string>();

    [MenuItem("Assets/CheckWeight")]
    public static void ShowWindow()
    {
        guids = Selection.assetGUIDs;
        paths.Clear();


        string path;
        foreach (string guid in guids)
        {
            if(istexture(path = AssetDatabase.GUIDToAssetPath(guid))) paths.Add(path);
        }

        if (paths.Count != 0) {
            resourcesFilter window = GetWindow<resourcesFilter>();
            window.titleContent = new GUIContent("console");

            foreach (string p in paths) {
                shows.Add(new node(p, isout(p)));
            }
        }
    }


    private Vector2 values;
    private float width = -1;

    private void OnGUI()
    {
        values = EditorGUILayout.BeginScrollView(values);

        foreach (node node in shows){
            EditorGUILayout.BeginHorizontal();

            EditorGUILayout.LabelField(node.name,GUILayout.MinWidth(Screen.width /2));
            EditorGUILayout.LabelField(""+node.isout);

            EditorGUILayout.EndHorizontal();
        }
        EditorGUILayout.EndScrollView();

        if (GUILayout.Button("Çå¿Õ")) { shows.Clear(); this.width = -1; }
    }


    private static string mat = ".mat";

    private static bool istexture(string ss)
    {
        if (ss == null || ss.Length < 4) return false;

        
        for (int i = 0; i < 4; i++)
        {
            if (!(ss[ss.Length - 4 + i] == mat[i])) return false;
        }

        return true;
    } 

    private static bool isout(string ss)
    {

        return false;
    }






}
