Shader "Hidden/fullscreenColor"
{
    Properties
    {
       _max ("Threshold",Range(0,1)) = 0.2
       _color ("Color",Color) = (0,0,0,1)
    }
    SubShader
    {
        Tags { "RenderType" = "Opaque" "RenderPipeline" = "UniversalPipeline" }
        Pass
        {
            Name "FullScreenBlit"

            HLSLPROGRAM
            #pragma vertex Vert
            #pragma fragment Frag
            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/Core.hlsl"
            #include "Packages/com.unity.render-pipelines.core/Runtime/Utilities/Blit.hlsl"

            struct attributes
            {
              uint vertexID : SV_VertexID;              
            };

             struct vary
            {
              float4 postionCS : SV_POSITION;
              float2 uv : TEXCOORD0;
            };

            vary Vert(attributes a)
            {
                vary r;
                r.postionCS = GetFullScreenTriangleVertexPosition(a.vertexID);
                r.uv = GetFullScreenTriangleTexCoord(a.vertexID);

                return r;
            }
           
            //Varyings
            half4 Frag(Varyings i) : SV_Target
            {
                return half4(1, 0, 0, 1); // È«ÆÁºìÉ«
            }
            ENDHLSL
        }
    }
}
